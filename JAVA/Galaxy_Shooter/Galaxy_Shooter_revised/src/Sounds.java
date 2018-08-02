import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioStream;

public class Sounds {

	private File beamSound = new File("sounds/alienBeam.wav");
	private File bulletSound = new File("sounds/bulletSound.wav");
	private File levelUpSound = new File("sounds/levelUpSound.wav");
	private File deathSound = new File("sounds/deathSound.wav");
	private File hitmarkerSound = new File("sounds/hitmarkerSound.wav");
	private File shieldSound = new File("sounds/shieldSound.wav");
	private File bossSound = new File("sounds/bossSound.wav");
	private File bonusSound = new File("sounds/bonusSound.wav");
	private File damageSound = new File("sounds/damageSound.wav");
	private AudioStream beamSoundAudio;
	private InputStream beamSoundInput;
	private AudioStream bulletSoundAudio;
	private InputStream bulletSoundInput;
	private AudioStream levelUpSoundAudio;
	private InputStream levelUpSoundInput;
	private AudioStream deathSoundAudio;
	private InputStream deathSoundInput;
	private AudioStream hitSoundAudio;
	private InputStream hitSoundInput;
	private AudioStream shieldSoundAudio;
	private InputStream shieldSoundInput;
	private AudioStream bossSoundAudio;
	private InputStream bossSoundInput;
	private AudioStream bonusSoundAudio;
	private InputStream bonusSoundInput;
	private AudioStream damageSoundAudio;
	private InputStream damageSoundInput;

	try
	{
		beamSoundInput = new FileInputStream(beamSound);
		beamSoundAudio = new AudioStream(beamSoundInput);
		bulletSoundInput = new FileInputStream(bulletSound);
		bulletSoundAudio = new AudioStream(bulletSoundInput);
		levelUpSoundInput = new FileInputStream(levelUpSound);
		levelUpSoundAudio = new AudioStream(levelUpSoundInput);
		deathSoundInput = new FileInputStream(deathSound);
		deathSoundAudio = new AudioStream(deathSoundInput);
		hitSoundInput = new FileInputStream(hitmarkerSound);
		hitSoundAudio = new AudioStream(hitSoundInput);
		shieldSoundInput = new FileInputStream(shieldSound);
		shieldSoundAudio = new AudioStream(shieldSoundInput);
		bossSoundInput = new FileInputStream(bossSound);
		bossSoundAudio = new AudioStream(bossSoundInput);
		bonusSoundInput = new FileInputStream(bonusSound);
		bonusSoundAudio = new AudioStream(bonusSoundInput);
		damageSoundInput = new FileInputStream(damageSound);
		damageSoundAudio = new AudioStream(damageSoundInput);
	}catch(
	IOException e)
	{

	}
}
