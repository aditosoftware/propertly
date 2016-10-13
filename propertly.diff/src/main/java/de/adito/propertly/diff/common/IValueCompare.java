package de.adito.propertly.diff.common;

import java.util.Objects;

/**
 * @author j.boesl, 19.07.2015
 */
public interface IValueCompare
{

  boolean equals(Object pO1, Object pO2);

  byte[] createEqualityHash(Object pO);

  /**
   * Simple IValueCompare implementation.
   */
  IValueCompare SIMPLE_IMPL = new IValueCompare()
  {
    @Override
    public boolean equals(Object pO1, Object pO2)
    {
      return Objects.equals(pO1, pO2);
    }

    @Override
    public byte[] createEqualityHash(Object pO)
    {
      return pO == null ? new byte[]{(byte) 0} : intToByteArray(pO.hashCode());
    }

    private byte[] intToByteArray(int pValue)
    {
      return new byte[]{(byte) (pValue >>> 24), (byte) (pValue >>> 16), (byte) (pValue >>> 8), (byte) pValue};
    }
  };

}
