package com.github.schuettec.server;

import java.util.Set;

import com.github.schuettec.remote.Service;
import com.github.schuettec.world.skills.Entity;

public class ServiceImpl implements Service {

	@Override
	public String echo(String message) {
		return new StringBuilder(message).reverse().toString();
	}

	@Override
	public Set<Entity> getView() {
		// TODO Auto-generated method stub
		return null;
	}

}
