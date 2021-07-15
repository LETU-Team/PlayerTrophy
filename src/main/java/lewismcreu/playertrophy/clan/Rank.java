package lewismcreu.playertrophy.clan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lewis_McReu
 */
public class Rank
{
	private String name;
	private Set<Right> rights;

	public Rank(String name)
	{
		this.rights = new HashSet<Right>();
		this.setName(name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addRight(Right right)
	{
		this.rights.add(right);
	}

	public boolean hasRight(Right right)
	{
		return this.rights.contains(right);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Rank)
		{
			Rank r = (Rank) obj;
			return r.getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public Collection<Right> getRights()
	{
		return this.rights;
	}

	public void removeRight(Right right)
	{
		this.rights.remove(right);
	}
}
