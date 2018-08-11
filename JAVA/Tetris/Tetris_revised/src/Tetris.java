
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class Tetris extends JFrame {
	/* 공용으로 사용되는 변수 */
	private static final long serialVersionUID = -4722429764792514382L;
	/**
	 * 타일 유형의 수
	 */
	private static final int TYPE_COUNT = TileType.values().length - 2;
	/**
	 * SidePanel 인스턴스.
	 */
	private SidePanel side;
	/**
	 * BottomPanel 인스턴스.
	 */
	private BottomPanel bottomPanel;
	/**
	 * 타일 생성 후 떨어지기 전까지 경과되는 시간
	 */
	private int dropCooldown;
	/**
	 * 타일 유형을 랜덤으로 지정
	 */
	private Random random;
	/**
	 * BoardPanel 인스턴스.
	 */
	/* 1P 구현을 위해 사용되는 변수 */
	private BoardPanel board;
	/**
	 * 게임의 일시 정지 여부.
	 */
	private boolean isPaused;
	/**
	 * 게임 시작 여부. (처음엔 true로 설정하고 게임을 시작하면 false로 설정.)
	 */
	private boolean isNewGame;
	/**
	 * 게임 오버 여부.
	 */
	private boolean isGameOver;
	/**
	 * 현재 레벨.
	 */
	private int level;
	/**
	 * 현재 점수.
	 */
	private int currentScore;
	/**
	 * 최초 점수.
	 */
	private int Score = 0;
	/**
	 * 레벨업을 위한 점수.
	 */
	private int upScore = 30;
	/**
	 * 업데이트 로직을 처리하는 시계.
	 */
	private Clock logicTimer;
	/**
	 * 현재 타일의 유형.
	 */
	private TileType currentType;
	/**
	 * 다음 타일의 유형.
	 */
	private TileType nextType;
	/**
	 * 다다음 타일의 유형.
	 */
	private TileType next2Type;
	/**
	 * 타일 유형을 임시 저장.
	 */
	private TileType tempType;
	/**
	 * 타일의 현재 열.
	 */
	private int currentCol;
	/**
	 * 타일의 현재 행.
	 */
	private int currentRow;
	/**
	 * 타일의 현재 회전.
	 */
	private int currentRotation;
	/**
	 * 게임 속도.
	 */
	private float gameSpeed;
	/* 2P 구현을 위해 사용되는 변수 */
	/**
	 * BoardPanel2 인스턴스.
	 */
	private BoardPanel2 board2;
	/**
	 * 게임 일시 정지 여부(2P).
	 */
	private boolean isPaused2p;

	/**
	 * 게임 시작 여부.(처음엔 true로 설정하고 게임을 시작하면 false로 설정(2P).)
	 */
	private boolean isNewGame2p;

	/**
	 * 게임오버 여부(2P).
	 */
	private boolean isGameOver2p;

	/**
	 * 현재 레벨(2P).
	 */
	private int level2p;
	/**
	 * 현재 점수(2P).
	 */
	private int currentScore2p;
	/**
	 * 최초 점수(2P).
	 */
	private int Score2p = 0;
	/**
	 * 레벨업을 위한 점수(2P).
	 */
	private int upScore2p = 30;
	/**
	 * 업데이트 로직을 처리하는 시계(2P).
	 */
	private Clock logicTimer2p;
	/**
	 * 현재 타일의 유형(2P).
	 */
	private TileType currentType2p;
	/**
	 * 다음 타일의 유형(2P).
	 */
	private TileType nextType2p;
	/**
	 * 다다음 타일의 유형(2P).
	 */
	private TileType next2Type2p;
	/**
	 * 타일을 임시 저장(2P).
	 */
	private TileType tempType2p;
	/**
	 * 타일의 현재 열(2P).
	 */
	private int currentCol2p;
	/**
	 * 타일의 현재 행(2P).
	 */
	private int currentRow2p;
	/**
	 * 타일의 현재 회전(2P).
	 */
	private int currentRotation2p;
	/**
	 * 게임의 속도(2P).
	 */
	private float gameSpeed2p;
	/**
	 * 게임 BGM
	 */
	private File sound;

	public Tetris() {
		// 윈도우의 기본 속성을 설정.
		setTitle("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		// BoardPanel과SidePanel 인스턴스를 초기화.
		this.board = new BoardPanel(this);
		this.board2 = new BoardPanel2(this);
		this.side = new SidePanel(this);
		this.bottomPanel = new BottomPanel(this);

		// BoardPanel과SidePanel 인스턴스를 윈도우에 추가.
		add(board, BorderLayout.WEST);
		add(side, BorderLayout.CENTER);
		add(board2, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);

		sound = new File("res/tetrismusic.wav");
		try {

			AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();

		} catch (Exception e) {

			e.printStackTrace();
		}

		// KeyListener를 추가
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				/**
				 * 1P 컨트롤
				 */
				// 빠른 드롭 - 게임이 정지상태가 아니고 dropCooldown이 없을때 스페이스바를 내리면 초당
				// 1000사이클의 속도로 로직 타이머가 설정됨.
				case KeyEvent.VK_SPACE:
					if (!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(1000.0f);
					}
					break;
				// 왼쪽 이동-게임이 정지상태가 아니고 왼쪽으로 이동할 수 있을때 왼쪽 방향키를 누르면 현재의 열을 1만큼 감소.
				case KeyEvent.VK_A:
					if (!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
						currentCol--;
					}
					break;
				// 오른쪽 이동-게임이 정지상태가 아니고 오른쪽으로 이동할 수 있을때 오른쪽 방향키를 누르면 현재의 열을 1만큼
				// 증가.
				case KeyEvent.VK_D:
					if (!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
						currentCol++;
					}
					break;
				// 위쪽 이동-게임이 정지상태가 아닐때 위쪽 방향키를 누르면 반 시계 방향으로 회전.
				// currentRotation이 0이면 currentRotation은 3이 되고 아니라면
				// currentRotation이 1만큼 감소.
				case KeyEvent.VK_W:
					if (!isPaused) {
						rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
					}
					break;
				// 아래쪽 이동-게임이 정지상태가 아니고 아래쪽으로 이동할 수 있을때 아래쪽 방향키를 누르면 현재의 행을 1만큼
				// 증가.
				case KeyEvent.VK_S:
					if (!isPaused && board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
						currentRow++;
					}
					break;

				// 일시정지-게임이 정지상태가 아니고 진행중일때 P버튼을 누르면 일시정지.
				case KeyEvent.VK_P:
					if ((!isGameOver && !isNewGame) || (isGameOver2p && !isNewGame2p)) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
						isPaused2p = !isPaused2p;
						logicTimer2p.setPaused(isPaused2p);
					}
					break;
				// 컨트롤 키 - 누르면 다음 타입과 다다음 타입이 체인지.
				case KeyEvent.VK_CONTROL:
					tempType = nextType;
					nextType = next2Type;
					next2Type = tempType;
					break;
				/**
				 * 2P 컨트롤
				 */
				// 빠른 드롭 - 게임이 정지상태가 아니고 dropCooldown이 없을때 스페이스바를 내리면 초당
				// 1000사이클의 속도로 로직 타이머가 설정됨.
				case KeyEvent.VK_SHIFT:
					if (BottomPanel.radiobtn2.isSelected() && !isPaused2p && dropCooldown == 0) {
						logicTimer2p.setCyclesPerSecond(1000.0f);
					}
					break;
				// 왼쪽 이동-게임이 정지상태가 아니고 왼쪽으로 이동할 수 있을때 왼쪽 방향키를 누르면 현재의 열을 1만큼 감소.
				case KeyEvent.VK_LEFT:
					if (BottomPanel.radiobtn2.isSelected() && !isPaused2p && board2.isValidAndEmpty(currentType2p,
							currentCol2p - 1, currentRow2p, currentRotation2p)) {
						currentCol2p--;
					}
					break;
				// 오른쪽 이동-게임이 정지상태가 아니고 오른쪽으로 이동할 수 있을때 오른쪽 방향키를 누르면 현재의 열을 1만큼
				// 증가.
				case KeyEvent.VK_RIGHT:
					if (BottomPanel.radiobtn2.isSelected() && !isPaused2p && board2.isValidAndEmpty(currentType2p,
							currentCol2p + 1, currentRow2p, currentRotation2p)) {
						currentCol2p++;
					}
					break;
				// 위쪽 이동-게임이 정지상태가 아닐때 위쪽 방향키를 누르면 반 시계 방향으로 회전.
				// currentRotation이 0이면 currentRotation은 3이 되고 아니라면
				// currentRotation이 1만큼 감소.
				case KeyEvent.VK_UP:
					if (BottomPanel.radiobtn2.isSelected() && !isPaused2p) {
						rotatePiece2p((currentRotation2p == 0) ? 3 : currentRotation2p - 1);
					}
					break;
				// 아래쪽 이동-게임이 정지상태가 아니고 아래쪽으로 이동할 수 있을때 아래쪽 방향키를 누르면 현재의 행을 1만큼
				// 증가.
				case KeyEvent.VK_DOWN:
					if (BottomPanel.radiobtn2.isSelected() && !isPaused2p && board2.isValidAndEmpty(currentType2p,
							currentCol2p, currentRow2p + 1, currentRotation2p)) {
						currentRow2p++;
					}
					break;
				// L 키 - 누르면 다음 타입과 다다음 타입이 체인지.
				case KeyEvent.VK_L:
					if (BottomPanel.radiobtn2.isSelected()) {
						tempType2p = nextType2p;
						nextType2p = next2Type2p;
						next2Type2p = tempType2p;
					}
					break;
				}
			}
		});

		// BoardPanel과 SidePanel 인스턴스를 윈도우에 팩하고 위치조절을 불가하게 설정하고 화면에 보여줌.
		// 프레임과 컴포넌트를 preferred size에 맞도록 크기를 조정하고 화면 중앙에 배치한 후 보이게 설정.
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * 게임 실행. 모두 초기화.
	 */
	public void startGame() {

		this.random = new Random();
		this.isNewGame = true;
		this.isNewGame2p = true;

		// 타이머 설정은 사용자가 게임실행을 하기전까지 유지.
		this.logicTimer = new Clock(gameSpeed);
		this.logicTimer2p = new Clock(gameSpeed2p);
		logicTimer.setPaused(true);
		logicTimer2p.setPaused(true);

		while (true) {
			// 로직 타이머를 업데이트.
			logicTimer.update();
			logicTimer2p.update();
			// 싸이클이 시간을 경과 했을때 게임을 업데이트하고 현재 타일을 이동.
			if (logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			if (logicTimer2p.hasElapsedCycle()) {
				updateGame2p();
			}
			// 필요한 dropCooldown 감소.
			if (dropCooldown > 0) {
				dropCooldown--;
			}
			// 유저에게 창을 보여줌.
			renderGame();
		}
	}

	/**
	 * 게임을 업데이트하고 대부분의 로직을 처리.
	 */
	private void updateGame() {

		// 타일이 다음 행으로 이동할 수 있는지 확인.
		if (board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			// 이동가능하면 현재의 행을 증가.
			currentRow++;
		} else {
			// 보드의 하단에 도달하거나 다른 타일에 도달하면 타일을 보드에 추가.
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			// 새 타일이 추가 되면 행이 제거되었는지 확인, 제거 되었으면 점수를 증가. (1행 당 10점)
			int cleared = board.checkLines();

			if (cleared > 0) {
				Score = cleared * 10;
				currentScore += Score;
			}
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			dropCooldown = 25;

			// 최고 레벨 10
			// 레벨 업을 위한 점수 이상 획득할 경우 레벨업, 속도 증가, 게임 보드 초기화.
			if (level != 10 && currentScore >= upScore) {
				gameSpeed += 0.5f;
				board.clear();
				upScore += 30;
				level += 1;
				logicTimer.setCyclesPerSecond(gameSpeed);
				logicTimer.reset();
				board.addLine(BoardPanel.ROW_COUNT - level, 7, 8);
			}
			// 새 타일 추가
			spawnPiece();
		}
	}

	private void updateGame2p() {
		// 타일이 다음 행으로 이동할 수 있는지 확인.
		if (board2.isValidAndEmpty(currentType2p, currentCol2p, currentRow2p + 1, currentRotation2p)) {
			// 이동가능하면 현재의 행을 증가.
			currentRow2p++;
		} else {
			// 보드의 하단에 도달하거나 다른 타일에 도달하면 타일을 보드에 추가.
			board2.addPiece(currentType2p, currentCol2p, currentRow2p, currentRotation2p);
			// 새 타일이 추가 되면 행이 제거되었는지 확인, 제거 되었으면 점수를 증가. (1행 당 10점)
			int cleared = board2.checkLines();

			if (cleared > 0) {
				Score2p = cleared * 10;
				currentScore2p = Score2p + currentScore2p;
			}
			logicTimer2p.setCyclesPerSecond(gameSpeed);
			logicTimer2p.reset();
			dropCooldown = 25;

			// 최고 레벨 10
			// 레벨 업을 위한 점수 이상 획득할 경우 레벨업, 속도 증가, 게임 보드 초기화.
			if (level != 10 && currentScore2p >= upScore2p) {
				gameSpeed2p += 0.5f;
				board2.clear();
				upScore2p = upScore2p + 30;
				level2p = level2p + 1;
				logicTimer2p.setCyclesPerSecond(gameSpeed2p);
				logicTimer2p.reset();
				board2.addLine(BoardPanel2.ROW_COUNT - level2p, 7, 8);
			}
			// 새 타일 추가
			spawnPiece2p();
		}
	}

	/**
	 * 보드 패널 및 사이드 패널 그리기
	 */
	private void renderGame() {
		board.repaint();
		side.repaint();
		board2.repaint();
	}

	/**
	 * 새로운 게임이 시작될 때 기본값으로 변수 재설정.
	 */
	void resetGame() {
		this.level = (int) bottomPanel.SpeedLevel;
		this.currentScore = 0;
		this.gameSpeed = 0.5f + (0.5f * bottomPanel.SpeedLevel);
		this.nextType = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.next2Type = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.isNewGame = false;
		this.isGameOver = false;
		upScore = 30;
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}

	void resetGame2p() {
		this.level2p = (int) bottomPanel.SpeedLevel;
		this.currentScore2p = 0;
		this.gameSpeed2p = 0.5f + (0.5f * bottomPanel.SpeedLevel);
		this.nextType2p = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.next2Type2p = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.isNewGame2p = false;
		this.isGameOver2p = false;
		upScore2p = 30;
		board2.clear();
		logicTimer2p.reset();
		logicTimer2p.setCyclesPerSecond(gameSpeed2p);
		spawnPiece2p();
	}

	/*
	 * 새 타일 생성 후 기본값으로 타일의 변수 재설정.
	 */
	private void spawnPiece() {
		// 다음 타일의의 변수로 현재 타일을 설정.
		this.currentType = nextType;
		this.nextType = next2Type;
		// enum.values() : 열거된 모든 원소를 배열에 담아 순서대로 반환
		this.next2Type = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;

		// 생성 지점이 유효하지 않으면 게임 오버.
		if (!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			// 1p 모드일때
			logicTimer.setPaused(true);
			if (BottomPanel.radiobtn1.isSelected()) {
				if (isGameOver) {
					this.isGameOver2p = true;
					IdPanel idpnel = new IdPanel(level, currentScore);
					idpnel.setVisible(true);
					bottomPanel.setVisible(true);
				}
			}
			// 2p 모드일때
			else if (BottomPanel.radiobtn2.isSelected()) {
				if (isGameOver && isGameOver2p) {
					IdPanel idpnel = new IdPanel(level, currentScore);
					IdPanel2 idpnel2 = new IdPanel2(level2p, currentScore2p);
					idpnel.setVisible(true);
					idpnel2.setVisible(true);
					bottomPanel.setVisible(true);
				}
			}
		}
	}

	private void spawnPiece2p() {
		// 다음 타일의 변수로 현재 타일을 설정.
		this.currentType2p = nextType2p;
		this.nextType2p = next2Type2p;
		this.next2Type2p = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.currentCol2p = currentType2p.getSpawnColumn();
		this.currentRow2p = currentType2p.getSpawnRow();
		this.currentRotation2p = 0;

		// 생성 지점이 유효하지 않으면 게임 오버.
		if (!board2.isValidAndEmpty(currentType2p, currentCol2p, currentRow2p, currentRotation2p)) {
			this.isGameOver2p = true;
			logicTimer2p.setPaused(true);
			if (isGameOver && isGameOver2p) {
				IdPanel idpnel = new IdPanel(level, currentScore);
				IdPanel2 idpnel2 = new IdPanel2(level2p, currentScore2p);
				idpnel.setVisible(true);
				idpnel2.setVisible(true);
				bottomPanel.setVisible(true);
			}

		}
	}

	/**
	 * 현재 타일을 회전하도록 설정.
	 * 
	 * @param newRotation : 타일의 새로운 회전.
	 */
	private void rotatePiece(int newRotation) {
		// 타일을 이동해야 할 경우 임시 행과 열을 저장.
		int newColumn = currentCol;
		int newRow = currentRow;

		// 측면 각각에 대한 인셋을 가져옴. (특정 측면의 빈 행과 열을 알기 위해 사용.)
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);

		// 현재 타일이 왼쪽이나 오른쪽 가장자리에 있는 경우 보드 밖으로 나가거나 무효가 되지 않도록 가장자리 반대편으로 타일 이동
		if (currentCol < -left) {
			newColumn -= currentCol - left;
		} else if (currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}

		// 현재의 타일이 아래쪽이나 위쪽 가장자리에 있는 경우 보드 밖으로 나가거나 무효가 되지 않도록 가장자리 반대편으로 타일 이동
		if (currentRow < -top) {
			newRow -= currentRow - top;
		} else if (currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}

		// 새로운 위치가 허용 되었는지 확인. (true인 경우 타일의 회전과 위치를 업데이트)
		if (board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}

	private void rotatePiece2p(int newRotation) {
		// 타일을 이동해야 할 경우 임시 행과 열을 저장.
		int newColumn = currentCol2p;
		int newRow = currentRow2p;

		// 측면 각각에 대한 인셋을 가져옴. (특정 측면의 빈 행과 열을 알기 위해 사용.)
		int left = currentType2p.getLeftInset(newRotation);
		int right = currentType2p.getRightInset(newRotation);
		int top = currentType2p.getTopInset(newRotation);
		int bottom = currentType2p.getBottomInset(newRotation);

		// 현재 타일이 왼쪽이나 오른쪽 가장자리에 있는 경우 보드 밖으로 나가거나 무효가 되지 않도록 가장자리 반대편으로 타일 이동
		if (currentCol2p < -left) {
			newColumn -= currentCol2p - left;
		} else if (currentCol2p + currentType2p.getDimension() - right >= BoardPanel2.COL_COUNT) {
			newColumn -= (currentCol2p + currentType2p.getDimension() - right) - BoardPanel2.COL_COUNT + 1;
		}

		// 현재의 타일이 아래쪽이나 위쪽 가장자리에 있는 경우 보드 밖으로 나가거나 무효가 되지 않도록 가장자리 반대편으로 타일 이동
		if (currentRow2p < -top) {
			newRow -= currentRow2p - top;
		} else if (currentRow2p + currentType2p.getDimension() - bottom >= BoardPanel2.ROW_COUNT) {
			newRow -= (currentRow2p + currentType2p.getDimension() - bottom) - BoardPanel2.ROW_COUNT + 1;
		}

		// 새로운 위치가 허용 되었는지 확인. (true인 경우 타일의 회전과 위치를 업데이트)
		if (board2.isValidAndEmpty(currentType2p, newColumn, newRow, newRotation)) {
			currentRotation2p = newRotation;
			currentRow2p = newRow;
			currentCol2p = newColumn;
		}
	}

	/* 1P 구현을 위한 메소드 */
	/**
	 * 게임 일시 정부 여부 반환.
	 * 
	 * @return 일시 정지 여부.
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * 게임오버 여부 반환.
	 * 
	 * @return 게임오버 여부.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * 게임 시작 여부 반환
	 * 
	 * @return 게임 시작 여부.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}

	/**
	 * 현재 점수 반환
	 * 
	 * @return 현재점수.
	 */
	public int getcurrentScore() {
		return currentScore;
	}

	/**
	 * 현재 레벨 반환
	 * 
	 * @return 현재 레벨.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 현재 타일 유형 반환
	 * 
	 * @return 현재 타일 유형.
	 */
	public TileType getPieceType() {
		return currentType;
	}

	/**
	 * 다음 타일 유형 반환
	 * 
	 * @return 다음 타일 유형
	 */
	public TileType getNextPieceType() {
		return nextType;
	}

	/**
	 * 다다음 타일 유형 반환
	 * 
	 * @return 다다음 타일 유형
	 */
	public TileType getNextPieceType2() {
		return next2Type;
	}

	/**
	 * 현재 타일의 열 반환
	 * 
	 * @return 현재 타일의 열.
	 */
	public int getPieceCol() {
		return currentCol;
	}

	/**
	 * 현재 타일의 행을 반환
	 * 
	 * @return 현재 타일의 행.
	 */
	public int getPieceRow() {
		return currentRow;
	}

	/**
	 * 현재 타일의 회전 수를 반환
	 * 
	 * @return 현재 타일의 회전 수.
	 */
	public int getPieceRotation() {
		return currentRotation;
	}

	/* 2P 구현을 위한 메소드 */
	/**
	 * 게임 일시 정지 여부 반환(2P).
	 * 
	 * @return 게임 일시 정지 여부.
	 */
	public boolean isPaused2p() {
		return isPaused2p;
	}

	/**
	 * 게임오버 여부 반환(2P).
	 * 
	 * @return 게임오버 여부.
	 */
	public boolean isGameOver2p() {
		return isGameOver2p;
	}

	/**
	 * 게임 시작 여부 반환(2P).
	 * 
	 * @return 일시 시작 여부.
	 */
	public boolean isNewGame2p() {
		return isNewGame2p;
	}

	/**
	 * 현재 점수를 반환(2P)
	 * 
	 * @return 현재 점수.
	 */
	public int getcurrentScore2p() {
		return currentScore2p;
	}

	/**
	 * 현재 레벨을 반환(2P).
	 * 
	 * @return 현재 레벨.
	 */
	public int getLevel2p() {
		return level2p;
	}

	/**
	 * 현재 타일의 유형 반환(2P).
	 * 
	 * @return 현재 타일 유형.
	 */
	public TileType getPieceType2p() {
		return currentType2p;
	}

	/**
	 * 다음 타일의 유형 반환(2P).
	 * 
	 * @return 다음 타일의 유형.
	 */
	public TileType getNextPieceType2p() {
		return nextType2p;
	}

	/**
	 * 다다음 타일의 유형 반환(2P).
	 * 
	 * @return 다다음 타일의 유형.
	 */
	public TileType getNextPieceType22p() {
		return next2Type2p;
	}

	/**
	 * 현재 타일의 열 반환(2P).
	 * 
	 * @return 현재 타일의 열.
	 */
	public int getPieceCol2p() {
		return currentCol2p;
	}

	/**
	 * 현재 타일의 행 반환(2P).
	 * 
	 * @return 현재 타일의 행.
	 */
	public int getPieceRow2p() {
		return currentRow2p;
	}

	/**
	 * 현재 타일의 회전 수를 반환(2P).
	 * 
	 * @return 현재 타일의 회전수
	 */
	public int getPieceRotation2p() {
		return currentRotation2p;
	}

	/**
	 * 테트리스 실행을 위한 메인 함수
	 */
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.startGame();

	}
}