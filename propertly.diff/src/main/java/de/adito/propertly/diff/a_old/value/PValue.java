package de.adito.propertly.diff.a_old.value;

import de.adito.propertly.core.spi.IPropertyPitProvider;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * @author j.boesl, 16.07.15
 */
public abstract class PValue
{

  private int size = -1;
  private byte[] hash;

  private String name;
  private Object value;


  PValue(String pName, Object pValue)
  {
    name = pName;
    value = pValue;
  }

  protected final void update()
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("sha");
      md.reset();
      size = recalculateSize(md);
      hash = md.digest();
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
  }

  public final int getSize()
  {
    return size;
  }

  public final byte[] getHash()
  {
    return hash;
  }


  protected int recalculateSize(MessageDigest pMessageDigest)
  {
    updateMD(pMessageDigest, name);
    int size = 1;

    if (value == null)
      pMessageDigest.update((byte) 0);
    else
    {
      updateMD(pMessageDigest, value.getClass().getCanonicalName());
      if (value instanceof IPropertyPitProvider)
        pMessageDigest.update((byte) 0);
      else
      {
        pMessageDigest.update((byte) value.hashCode());
        size++;
      }
    }
    return size;
  }

  protected final void updateMD(MessageDigest pMd, String pStr)
  {
    try
    {
      pMd.update(pStr.getBytes("utf-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }

}
