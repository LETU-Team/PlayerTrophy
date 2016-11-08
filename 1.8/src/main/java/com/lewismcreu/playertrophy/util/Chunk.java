package com.lewismcreu.playertrophy.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class Chunk
{
	public final int x;
	public final int z;

	public static Chunk getChunkFromPlayer(EntityPlayer player)
	{
		return new Chunk(player.chunkCoordX, player.chunkCoordZ);
	}

	public static Chunk getChunkFromPos(BlockPos pos)
	{
		return new Chunk(pos.getX() >> 4, pos.getZ() >> 4);
	}

	public Chunk(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public boolean isPlayerWithin(EntityPlayer player)
	{
		return player.chunkCoordX == x && player.chunkCoordZ == z;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Chunk)
		{
			Chunk chunk = (Chunk) obj;
			return chunk.x == x && chunk.z == z;
		}
		return false;
	}
}
