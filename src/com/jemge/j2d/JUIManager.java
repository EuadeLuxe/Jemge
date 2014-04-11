package com.jemge.j2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jemge.core.EngineModule;

public class JUIManager extends EngineModule {
	private Stage stage;

	public void init() {
		stage = new Stage();
	}

	@Override
	public void update() {
	}

	public void render() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}
}