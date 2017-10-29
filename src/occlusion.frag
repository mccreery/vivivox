in vec2 myTexCoord;
uniform sampler2D heightTex;
uniform sampler2D normalTex;
uniform sampler3D occTex;

out vec4 occlusionOut;
vec3 unProj(vec2 screenPos, float height) {
  return vec3((1.000000E+00 + screenPos.x*-1.302083E-03)*height, (5.625000E-01 + screenPos.y*-1.302083E-03)*height, height);
}
bool insideTex(vec2 pos) {
  return ((pos.x > 5.000000E-01) && (pos.x < 1.535500E+03) && (pos.y > 5.000000E-01) && (pos.y < 8.635000E+02));
}

float rand(vec2 co) {
  return fract(sin(dot(co.xy, vec2(12.9898,78.233)))*43758.5453);
}

void main() {
  float occlusion = 0.0f;
  int tidX = int(myTexCoord.x*1.280000E+03f);
  int tidY = int(myTexCoord.y*7.200000E+02f);
  vec2 myPos = vec2(1.285000E+02 + float(tidX), 7.915000E+02 - float(tidY));
  float myHeight = texture(heightTex, vec2(myPos.x*6.510417E-04, myPos.y*1.157407E-03)).x;
  vec3 p = unProj(myPos, myHeight);
  float third1 = 1.000000E+00 + -1.302083E-03*myPos.x;
  float third2 = 5.625000E-01 + -1.302083E-03*myPos.y;
  vec3 pUp = normalize(unProj(myPos, myHeight+1.0) - p);

  float totalSectorContribution = 0.0f;
  vec3 n;
  {
    vec4 nGlobal = texture(normalTex, vec2(myPos.x*6.510417E-04, myPos.y*1.157407E-03));
    float normCoef = inversesqrt(nGlobal.x*nGlobal.x + nGlobal.y*nGlobal.y + nGlobal.z*nGlobal.z);
    n = vec3(nGlobal.x*normCoef, nGlobal.y*normCoef, nGlobal.z*normCoef);
  }


  for (int dir = 0; dir < 16; ++dir) {
    vec2 dirStep;
    {
      float angle = rand(myPos)*3.926991E-01 + float(dir)*3.926991E-01;
      dirStep.x = sin(angle);
      dirStep.y = cos(angle);
    }

    float thisSectorContribution, zCoef, nStartCoord, nProjLen;
    vec2 pUpLocal;
    {
      float second = dirStep.y*third1 - dirStep.x*third2;
      thisSectorContribution = 1.0/(1.0 + second*second);
      float first = thisSectorContribution*(n.y*dirStep.x - n.x*dirStep.y + n.z*second);
      vec3 nProj = vec3(n.x + dirStep.y*first, n.y - dirStep.x*first, n.z - second*first);
      float nProjDot = dot(nProj, nProj);
      nProjLen = sqrt(nProjDot);
      vec3 flipUp = vec3(second*third2 - dirStep.x, -dirStep.y - second*third1, dirStep.x*third1 + dirStep.y*third2);
      float nProjFlip_cos = -dot(flipUp, nProj)*inversesqrt(nProjDot*dot(flipUp, flipUp));
      int nCos_quantized = int(nProjFlip_cos*6.350000E+01 + 6.399000E+01);
      nStartCoord = 7.812500E-03*float(nCos_quantized) + 3.906250E-03;
      pUpLocal.x = dirStep.x*pUp.x + dirStep.y*pUp.y;
      pUpLocal.y = sqrt(1.0f - pUpLocal.x*pUpLocal.x);
      zCoef = inversesqrt(thisSectorContribution);
    }
    totalSectorContribution += thisSectorContribution;
    vec2 pLocal = vec2(dirStep.x*p.x + dirStep.y*p.y, p.z*zCoef);
    float dirOcclusion = 0.0;
    float slope = 0.0;
    float borderD;
    {
      float nearX = myPos.x/(1.989124E-01 - dirStep.x);
      float nearY = myPos.y/(1.989124E-01 - dirStep.y);
      borderD = (nearX > 0.0) ? nearX : (1.536000E+03 - myPos.x)/(1.989124E-01 + dirStep.x);
      borderD = (nearY > 0.0) ? min(borderD, nearY) : min(borderD, (8.640000E+02 - myPos.y)/(1.989124E-01 + dirStep.y));
    }
    vec2 traverseStep = dirStep*7.593750E+00;
    for (vec2 traversePos = myPos + dirStep*2.078125E+01; length(traversePos - myPos) < borderD; traverseStep *= 1.500000E+00, traversePos += traverseStep) {
      float traverseHeight = texture(heightTex, vec2(traversePos.x*6.510417E-04, traversePos.y*1.157407E-03)).x;
      vec3 traverseProj = unProj(traversePos, traverseHeight);
      vec2 layerTopPos = vec2(dirStep.x*traverseProj.x + dirStep.y*traverseProj.y, traverseProj.z*zCoef);
      vec2 occVec = normalize(layerTopPos - pLocal);
      float newSlope = dot(occVec, pUpLocal)*4.843750E-01 + 5.000000E-01;
      if (newSlope > slope) {
        vec2 layerVec = normalize(layerTopPos);
        float slabSlant = dot(layerVec, pUpLocal)*-6.444009E-03 + -2.781899E-03;
        float slabDist = layerVec.y*pLocal.x - layerVec.x*pLocal.y;
        float occCoordX = nStartCoord + slabSlant;
        float occCoordZ = slabDist*1.585878E-01 + 1.562500E-02;
        vec2 cutLayerTopPos = layerTopPos + normalize(layerTopPos)*1.300000E+00;
        float cutSlope = dot(normalize(cutLayerTopPos - pLocal), pUpLocal)*4.843750E-01 + 5.000000E-01;
        dirOcclusion += texture(occTex, vec3(occCoordX, newSlope, occCoordZ)).x - texture(occTex, vec3(occCoordX, max(slope, cutSlope), occCoordZ)).x;
      }
      slope = max(newSlope, slope);
    }
    vec2 traversePos = myPos + dirStep*borderD;
      float traverseHeight = texture(heightTex, vec2(traversePos.x*6.510417E-04, traversePos.y*1.157407E-03)).x;
      vec3 traverseProj = unProj(traversePos, traverseHeight);
      vec2 layerTopPos = vec2(dirStep.x*traverseProj.x + dirStep.y*traverseProj.y, traverseProj.z*zCoef);
      vec2 occVec = normalize(layerTopPos - pLocal);
      float newSlope = dot(occVec, pUpLocal)*4.843750E-01 + 5.000000E-01;
      if (newSlope > slope) {
        vec2 layerVec = normalize(layerTopPos);
        float slabSlant = dot(layerVec, pUpLocal)*-6.444009E-03 + -2.781899E-03;
        float slabDist = layerVec.y*pLocal.x - layerVec.x*pLocal.y;
        float occCoordX = nStartCoord + slabSlant;
        float occCoordZ = slabDist*1.585878E-01 + 1.562500E-02;
        vec2 cutLayerTopPos = layerTopPos + normalize(layerTopPos)*1.300000E+00;
        float cutSlope = dot(normalize(cutLayerTopPos - pLocal), pUpLocal)*4.843750E-01 + 5.000000E-01;
        dirOcclusion += texture(occTex, vec3(occCoordX, newSlope, occCoordZ)).x - texture(occTex, vec3(occCoordX, max(slope, cutSlope), occCoordZ)).x;
      }
      slope = max(newSlope, slope);
    occlusion += dirOcclusion*nProjLen*thisSectorContribution;
  }
  occlusion = occlusion/totalSectorContribution*3.189470E+00;
  occlusionOut = vec4(1.0 - occlusion, 1.0 - occlusion, 1.0 - occlusion, 1.0);
}
