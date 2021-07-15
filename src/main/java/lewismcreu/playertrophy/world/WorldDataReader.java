package lewismcreu.playertrophy.world;

import lewismcreu.playertrophy.clan.Clan;
import lewismcreu.playertrophy.clan.Rank;
import lewismcreu.playertrophy.clan.Right;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Lewis_McReu
 */
public class WorldDataReader
{
	private Document doc;
	private Element xml;

	WorldDataReader(File worldDataFile)
	{
		try
		{
			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			DocumentBuilder dom = fac.newDocumentBuilder();
			doc = dom.parse(worldDataFile);
			xml = doc.getDocumentElement();
		}
		catch (SAXException | IOException | ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public HashMap<String, Clan> getClans()
	{
		HashMap<String, Clan> clans = new HashMap<String, Clan>();
		if (xml != null)
		{
			Element clansNode = (Element) xml.getElementsByTagName("clans").item(0);
			NodeList clanList = clansNode.getElementsByTagName("clan");
			for (int i = 0; i < clanList.getLength(); i++)
			{
				Element clanEl = (Element) clanList.item(i);

				// Get id and name
				String clanName = clanEl.getAttribute("name");

				// Get all ranks
				NodeList rankList = clanEl.getElementsByTagName("rank");
				Collection<Rank> ranks = new HashSet<Rank>();
				Rank defaultRank = null;
				for (int j = 0; j < rankList.getLength(); j++)
				{
					Element rankEl = (Element) rankList.item(j);
					String rankName = rankEl.getAttribute("name");
					boolean def = Boolean.getBoolean(rankEl.getAttribute("default"));
					Rank rank = new Rank(rankName);
					NodeList rightList = rankEl.getElementsByTagName("right");
					for (int k = 0; k < rightList.getLength(); k++)
					{
						rank.addRight(Right.valueOf(((Element) rightList.item(k))
								.getAttribute("name")));
					}
					if (def) defaultRank = rank;
					ranks.add(rank);
				}

				// Get all members
				NodeList memberList = clanEl.getElementsByTagName("member");
				Map<UUID, String> members = new HashMap<UUID, String>();
				for (int j = 0; j < memberList.getLength(); j++)
				{
					Element member = (Element) memberList.item(j);
					String uuid = member.getAttribute("uuid");
					String rank = member.getAttribute("rank");
					members.put(UUID.fromString(uuid), rank);
				}
				Clan clan = new Clan(clanName, ranks, members, defaultRank);
				clans.put(clan.getName(), clan);
			}
		}
		return clans;
	}

	public HashMap<UUID, Integer> getBounties()
	{
		HashMap<UUID, Integer> bounties = new HashMap<UUID, Integer>();
		if (xml != null)
		{
			Element bountiesNode = (Element) xml.getElementsByTagName("bounties").item(0);
			NodeList bountyList = bountiesNode.getElementsByTagName("bounty");
			for (int i = 0; i < bountyList.getLength(); i++)
			{
				Element bountyEl = (Element) bountyList.item(i);
				String uuid = bountyEl.getAttribute("uuid");
				int count = Integer.parseInt(bountyEl.getAttribute("count"));
				bounties.put(UUID.fromString(uuid), count);
			}
		}
		return bounties;
	}
}
