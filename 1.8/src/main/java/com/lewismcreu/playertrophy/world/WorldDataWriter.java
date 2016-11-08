package com.lewismcreu.playertrophy.world;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.clan.Rank;
import com.lewismcreu.playertrophy.clan.Right;
import com.lewismcreu.playertrophy.util.Chunk;

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
			newLine(str);
			str.writeStartElement("playertrophy");
			insertClans(clans);
			insertBounties(bounties);
			newLine(str);
			str.writeEndElement();
			newLine(str);
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
		newLine(str);
		str.writeStartElement("clans");
		for (Clan clan : clans.values())
		{
			if (clan.getMembers().size() > 0)
			{
				newLine(str);
				str.writeStartElement("clan");
				str.writeAttribute("name", clan.getName());
				Iterator<Rank> itR = clan.getRanks().iterator();
				Rank defaultRank = clan.getDefaultRank();
				while (itR.hasNext())
				{
					Rank rank = itR.next();
					newLine(str);
					str.writeStartElement("rank");
					str.writeAttribute("name", rank.getName());
					Iterator<Right> rights = rank.getRights().iterator();
					newLine(str);
					while (rights.hasNext())
					{
						str.writeStartElement("right");
						str.writeAttribute("name", rights.next().toString());
						if (rank.equals(defaultRank)) str.writeAttribute("default", "true");
						str.writeEndElement();
						newLine(str);
					}
					str.writeEndElement();
				}

				Iterator<Entry<UUID, Rank>> itM = clan.getMembers().iterator();
				newLine(str);
				while (itM.hasNext())
				{
					Entry<UUID, Rank> e = itM.next();
					str.writeStartElement("member");
					str.writeAttribute("uuid", e.getKey().toString());
					str.writeAttribute("rank", e.getValue().toString());
					str.writeEndElement();
					newLine(str);
				}

				Iterator<Chunk> itCh = clan.getChunks().iterator();
				newLine(str);
				while (itCh.hasNext())
				{
					Chunk c = itCh.next();
					str.writeStartElement("chunk");
					str.writeAttribute("x", c.x + "");
					str.writeAttribute("z", c.z + "");
					str.writeEndElement();
					newLine(str);
				}
				str.writeEndElement();
			}
		}
		newLine(str);
		str.writeEndElement();
	}

	private void insertBounties(HashMap<UUID, Integer> bounties) throws XMLStreamException
	{
		newLine(str);
		str.writeStartElement("bounties");
		Iterator<Entry<UUID, Integer>> itB = bounties.entrySet().iterator();
		newLine(str);
		while (itB.hasNext())
		{
			Entry<UUID, Integer> e = itB.next();
			str.writeStartElement("bounty");
			str.writeAttribute("uuid", e.getKey().toString());
			str.writeAttribute("count", e.getValue().toString());
			str.writeEndElement();
			newLine(str);
		}
		str.writeEndElement();
	}

	private void newLine(XMLStreamWriter str) throws XMLStreamException
	{
		str.writeCharacters(System.lineSeparator());
	}
}
