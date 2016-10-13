package de.adito.propertly.diff.delta;

import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.diff.data.*;

/**
 * @author j.boesl, 20.07.15
 */
public abstract class Delta
{

  public abstract void applyTo(OrgPatchedMapping pOrgPatchedMapping, IHierarchy pTo);

}
