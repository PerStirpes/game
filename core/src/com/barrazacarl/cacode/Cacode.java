package com.barrazacarl.cacode;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import javax.xml.soap.Text;


public class Cacode extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture gameover;

	Texture[] cacode;

	int flapState = 0;
	int pause = 0;
	float cacodeY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	float gravity = 1.5f;

	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");

		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		cacode = new Texture[8];

		cacode[0] = new Texture("cacode1.png");
		cacode[1] = new Texture("cacode2.png");
		cacode[2] = new Texture("cacode3.png");
		cacode[3] = new Texture("cacode4.png");
		cacode[4] = new Texture("cacode5.png");
		cacode[5] = new Texture("cacode6.png");
		cacode[6] = new Texture("cacode7.png");
		cacode[7] = new Texture("cacode8.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];


		startGame();


	}

	public void startGame() {

		cacodeY = Gdx.graphics.getHeight() / 2 - cacode[0].getHeight() / 2;

		for (int i = 0; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if (scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = -20;
			}

			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}


			if (cacodeY > 0) {
				velocity = velocity + gravity;
				cacodeY -= velocity;

			} else {
				gameState = 2;
			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}

		} else if (gameState == 2) {
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}

		}
		if (pause < 10){
			pause++;
		} else {
			pause = 0;
			if (flapState < 5) {
				flapState++;
			} else {
				flapState = 0;
			}
		}
		if (gameState == 2){
			batch.draw(cacode[6], Gdx.graphics.getWidth() / 2 - cacode[6].getWidth() / 2, cacodeY);
		} else {
			batch.draw(cacode[flapState], Gdx.graphics.getWidth() / 2 - cacode[flapState].getWidth() / 2, cacodeY);
		}


		font.draw(batch, String.valueOf(score), 100, 200);

		birdCircle.set(Gdx.graphics.getWidth() / 2, cacodeY + cacode[flapState].getHeight() / 2, cacode[flapState].getWidth() / 2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		for (int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = 2;
			}
		}


		batch.end();
		//shapeRenderer.end();
	}
}