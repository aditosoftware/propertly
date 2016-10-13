package de.adito.propertly.diff.a_old.value;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;

/**
 * @author j.boesl, 16.07.15
 */
public class PValueDynamic extends PValue
{

  private Class source;
  private Class type;
  private Annotation[] annotations;

  public PValueDynamic(String pName, Class pSource, Class pType, Annotation[] pAnnotations, Object pValue)
  {
    super(pName, pValue);
    source = pSource;
    type = pType;
    annotations = pAnnotations;
    update();
  }

  @Override
  protected int recalculateSize(MessageDigest pMessageDigest)
  {
    int size = super.recalculateSize(pMessageDigest);

    updateMD(pMessageDigest, source.getCanonicalName());
    size++;
    updateMD(pMessageDigest, type.getCanonicalName());
    size++;
    for (Annotation annotation : annotations)
    {
      pMessageDigest.update((byte) annotation.hashCode());
      size++;
    }
    return size;
  }

}
