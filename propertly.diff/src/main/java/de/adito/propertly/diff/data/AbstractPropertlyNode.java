package de.adito.propertly.diff.data;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.Map;

/**
 * @author j.boesl, 16.07.15
 */
public abstract class AbstractPropertlyNode<T,  S extends AbstractPropertlyNode<T, S>> implements IPropertlyNode<T, S>
{

  private Integer id;


  protected AbstractPropertlyNode(Integer pId)
  {
    id = pId;
  }

  protected final void update(Map<String, Integer> pHashIdMapping)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("sha");
      md.reset();

      if (updateMessageDigest(md))
      {
        byte[] digest = md.digest();
        String hexHash = DatatypeConverter.printHexBinary(digest);
        Integer mappingHash = pHashIdMapping.get(hexHash);
        if (mappingHash == null)
        {
          mappingHash = pHashIdMapping.size();
          pHashIdMapping.put(hexHash, mappingHash);
        }
        id = mappingHash;
      }
      else
        id = -1;

      md.reset();
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public final int getId()
  {
    return id;
  }

  protected abstract boolean updateMessageDigest(MessageDigest pMessageDigest);

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + "{" + getPath() + "}";
  }
}
