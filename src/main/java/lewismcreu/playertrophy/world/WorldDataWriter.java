package lewismcreu.playertrophy.world;

import lewismcreu.playertrophy.clan.Clan;
import lewismcreu.playertrophy.clan.Rank;
import lewismcreu.playertrophy.clan.Right;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * @author Lewis_McReu
 */
public class WorldDataWriter
{
	private XMLStreamWriter str;

	public WorldDataWriter(File worldDataFile, HashMap<String, Clan> clans, HashMap<UUID, Integer> bounties)
	{
		try
		{
			XMLOutputFactory fac = XMLOutputFactory.newFactory();
			str = fac.createXMLStreamWriter(new PrintWriter(worldDataFile));
			str.writeStartDocument();
			str.writeStartElement("playertrophy");
			insertClans(clans);
			insertBounties(bounties);
			str.writeEndElement();
			str.writeEndDocument();
			str.flush();
			str.close();
		}
		catch (IOException | XMLStreamException e)
		{
			e.printStackTrace();
		}
	}

	private void insertClans(HashMap<String, Clan> clans) throws XMLStreamException
	{
		str.writeStartElement("clans");
		for (Clan clan : clans.values())
		{
			if (clan.getMembers().size() > 0)
			{
				str.writeStartElement("clan");
				str.writeAttribute("name", clan.getName());
				Iterator<Rank> itR = clan.getRanks().iterator();
				Rank defaultRank = clan.getDefaultRank();
				while (itR.hasNext())
				{
					Rank rank = itR.next();
					str.writeStartElement("rank");
					str.writeAttribute("name", rank.getName());
					Iterator<Right> rights = rank.getRights().iterator();
					while (rights.hasNext())
					{
						str.writeStartElement("right");
						str.writeAttribute("name", rights.next().toString());
						if (rank.equals(defaultRank)) str.writeAttribute("default", "true");
						str.writeEndElement();
					}
					str.writeEndElement();
				}

				Iterator<Entry<UUID, Rank>> itM = clan.getMembers().iterator();
				while (itM.hasNext())
				{
					Entry<UUID, Rank> e = itM.next();
					str.writeStartElement("member");
					str.writeAttribute("uuid", e.getKey().toString());
					str.writeAttribute("rank", e.getValue().toString());
					str.writeEndElement();
				}
				str.writeEndElement();
			}
		}
		str.writeEndElement();
	}

	private void insertBounties(HashMap<UUID, Integer> bounties) throws XMLStreamException
	{
		str.writeStartElement("bounties");
		Iterator<Entry<UUID, Integer>> itB = bounties.entrySet().iterator();
		while (itB.hasNext())
		{
			Entry<UUID, Integer> e = itB.next();
			str.writeStartElement("bounty");
			str.writeAttribute("uuid", e.getKey().toString());
			str.writeAttribute("count", e.getValue().toString());
			str.writeEndElement();
		}
		str.writeEndElement();
	}
}
