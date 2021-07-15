package lewismcreu.playertrophy.tileentity;

import lewismcreu.playertrophy.common.data.Clan;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

/**
 * @author Lewis_McReu
 */
public class ProtectedTileEntityFactory
{
	public static IProtectedTileEntity createProtectedTileEntity(Clan clan,
			TileEntity tileEntity)
	{
		if (tileEntity instanceof TileEntityChest)
		{
			return new TileEntityProtectedChest(clan, (TileEntityChest) tileEntity);
		}
		else return null;
	}
}
