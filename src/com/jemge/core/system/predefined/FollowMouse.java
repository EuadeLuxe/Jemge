package com.jemge.core.system.predefined;

import com.jemge.core.system.IUpdateListener;
import com.jemge.input.InputManager;
import com.jemge.j2d.JSprite;

public class FollowMouse implements IUpdateListener<JSprite> {
	@Override
	public void update(JSprite object) {
		object.setPosition(
				InputManager.getInputPosition().x - (object.getWidth() / 2),
				InputManager.getInputPosition().y - (object.getHeight() / 2));
	}
}