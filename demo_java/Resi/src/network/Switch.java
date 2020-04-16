package network;

import config.Constant;
import network.host.Host;
import routing.RoutingAlgorithm;

import java.util.*;

/**
 * Created by Dandoh on 6/27/17.
 */
public class Switch extends Node {
	public int numPorts = 0;
	public Switch(int id)
	{
		super(id);
	}
}