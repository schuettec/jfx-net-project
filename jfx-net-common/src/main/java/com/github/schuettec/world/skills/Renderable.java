package com.github.schuettec.world.skills;

import com.github.schuettec.math.Rectangle;

public interface Renderable extends Entity {

	public boolean isVisible(Rectangle viewport);

}
