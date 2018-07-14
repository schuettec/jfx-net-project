package com.github.schuettec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.schuettec.remote.Service;

public class GameClient {
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		Kryo kryo = client.getKryo();
		// Register classes if needed
		kryo.register(Service.class);
		ObjectSpace.registerClasses(kryo);
		client.start();
		client.connect(5000, "localhost", 54555, 54777);

		Service someObject = ObjectSpace.getRemoteObject(client, 42, Service.class);
		String returnedMessage = someObject.echo("A message!");
		System.out.printf("Returned message: %s\n", returnedMessage);
	}

}
