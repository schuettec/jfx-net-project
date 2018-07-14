package com.github.schuettec.remote;

import java.util.Set;

import com.github.schuettec.world.skills.Entity;

public interface Service {

	public String echo(String message);

	public Set<Entity> getView();
}
