package Tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.Math;

import javax.swing.JPanel;

public class BoardPanel2 extends JPanel {

	private static final long serialVersionUID = 5055679736784226108L;
	/**
	 * 타일 최소 색상 구성 요소 값.
	 */
	public static final int COLOR_MIN = 35;
	/**
	 * 타일에 대한 최대 색 성분 값.
	 */
	public static final int COLOR_MAX = 255 - COLOR_MIN;
	/**
	 * 게임 보드의 테두리의 폭.
	 */
	private static final int BORDER_WIDTH = 5;
	/**
	 * 보드의 열 수.
	 */
	public static final int COL_COUNT = 10;
	/**
	 * 보드에 표시되는 행의 수.
	 */
	static final int VISIBLE_ROW_COUNT = 20;
	/**
	 * 숨겨진 행의 수.
	 */
	private static final int HIDDEN_ROW_COUNT = 2;
	/**
	 * 모든 행을 포함한 총 행의 수.
	 */
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	/**
	 * 타일이 차지하는 픽셀의 수.
	 */
	public static final int TILE_SIZE = 24;
	/**
	 * 게임 보드의 중앙 x 좌표.
	 */
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	/**
	 * 게임 보드의 중앙 y 좌표.
	 */
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
	/**
	 * 패널 총 폭.
	 */
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	/**
	 * 패널의 총 높이.
	 */
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	/**
	 * 큰 폰트.
	 */
	private static final Font LARGE_FONT = new Font("Verdana", Font.BOLD, 16);
	/**
	 * 작은 폰트.
	 */
	private static final Font SMALL_FONT = new Font("Verdana", Font.BOLD, 12);
	/**
	 * 테트리스 클래스의 인스턴스.
	 */
	private Tetris tetris;
	/**
	 * 보드를 구성하는 타일.
	 */
	private TileType[][] tiles;
	/**
	 * 레벨업시 랜덤 타입.
	 */
	private TileType RandomType;

	/**
	 * 새로은 게임 보드 인스턴스를 생성.
	 * 
	 * @param tetris
	 *            : Tetris 인스턴트를 사용 가능.
	 */
	public BoardPanel2(Tetris tetris) {
		this.tetris = tetris;
		this.tiles = new TileType[ROW_COUNT][COL_COUNT];

		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.WHITE);
	}

	/**
	 * 보드를 초기화하고 타일을 지움.
	 */
	public void clear() {
		// 모든 타일의 값을 null로 설정할때까지 반복.
		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}

	/**
	 * 블럭이 좌표에 배치 될 수 있는지 결정.
	 * 
	 * @param type
	 *            : 사용하는 타일의 타입.
	 * @param x
	 *            : 타일의 x 좌표.
	 * @param y
	 *            : 타일의 y 좌표.
	 * @param rotation
	 *            : 타일의 회전.
	 * @return 위치가 유효한지 여부.
	 */
	public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {
		// 타일이 유효한 열에 있는지 확인.
		if (x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		// 타일이 유효한 행에 있는지 확인.
		if (y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}
		// 기존의 타일과 충돌하는지 확인.
		for (int col = 0; col < type.getDimension(); col++) {
			for (int row = 0; row < type.getDimension(); row++) {
				if (type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 게임 보드에 타일을 추가.
	 * 
	 * @param type
	 *            : 배치된 타일의 타입.
	 * @param x
	 *            : 타일의 x 좌표.
	 * @param y
	 *            : 타일의 y 좌표.
	 * @param rotatrion
	 *            : 타일의 회전.
	 */
	public void addPiece(TileType type, int x, int y, int rotation) {

		for (int col = 0; col < type.getDimension(); col++) {
			for (int row = 0; row < type.getDimension(); row++) {
				if (type.isTile(col, row, rotation)) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}

	/**
	 * 몇 개의 행이 삭제 되었는지 확인하고 게임에서 삭제.
	 * 
	 * @return 삭제 된 행의 수.
	 */
	public int checkLines() {
		int completedLines = 0;

		// 모든 라인을 반복하고 삭제된 행이 있는지 검사.
		// 삭제 된 경우 완료된 라인의 개수를 증가하고 다음 행을 확인. checkLine 기능은 라인을 삭제하고 나머지를 한칸 아래로
		// 이동.
		for (int row = 0; row < ROW_COUNT; row++) {
			if (checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}

	/**
	 * 행이 가득차는지 확인.
	 * 
	 * @param line
	 *            : 행을 확인.
	 * @return 행이 가득차는지 여부.
	 */
	private boolean checkLine(int line) {
		// 행의 모든 열을 반복. 그 중 하나라도 비어있는 경우, 그 행은 풀이 아님.
		for (int col = 0; col < COL_COUNT; col++) {
			if (!isOccupied(col, line)) {
				return false;
			}
		}
		// 한 라인이 그려졌으면 그 선위의 모든 행을 아래로 이동.
		for (int row = line - 1; row >= 0; row--) {
			for (int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
		}
		return true;
	}

	/**
	 * 랜덤 행을 추가.
	 * 
	 * @param line
	 *            : 행을 확인.
	 * @param min
	 *            : 타일의 최소값.
	 * @param max
	 *            : 타일의 최대값
	 * @return 행이 가득차는지 여부.
	 */
	public boolean addLine(int line, int min, int max) {
		// 행이 전부 이어졌는지 확인.
		for (int row = 0; row < ROW_COUNT; row++) {
			for (int col = 0; col < COL_COUNT; col++) {
				if (isOccupied(col, line)) {
					return false;
				}
			}
		}
		// 레벨 만큼 랜덤으로 행 생성.
		for (int row = line; row < ROW_COUNT; row++) {
			for (int col = 0; col < COL_COUNT; col++) {
				this.RandomType = TileType.values()[(int) (Math.random() * (max - min + 1)) + min];
				if (RandomType != TileType.Type8) {
					setTile(col, row, RandomType);
				}
			}
		}
		return true;
	}

	/**
	 * 타일이 이미 있는지 확인합니다.
	 * 
	 * @param x
	 *            : x좌표 확인.
	 * @param y
	 *            : y좌표 확인.
	 * @return 타일이 있는지 여부.
	 */
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
	}

	/**
	 * 원하는 행과 열에 있는 타일을 설정합니다.
	 * 
	 * @param x
	 *            : 열.
	 * @param y
	 *            : 행.
	 * @param type
	 *            : 타일을 설정하는 값.
	 */
	private void setTile(int x, int y, TileType type) {
		tiles[y][x] = type;
	}

	/**
	 * 열과 행으로 타일을 겟.
	 * 
	 * @param x
	 *            : 열.
	 * @param y
	 *            : 행.
	 * @return 타일.
	 */
	private TileType getTile(int x, int y) {
		return tiles[y][x];
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 위치를 단순화.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);

		// 현재 게임 상태에 따라 다르게 보드를 그림.
		if (tetris.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.BLACK);
			String msg = "PAUSED";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
		} else if (tetris.isNewGame2p() || tetris.isGameOver2p()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.BLACK);
			// 게임오버와 새게임 화면은 거의 동일해서 함께 처리하기 위해 삼항 연산자 사용.
			String msg = tetris.isNewGame2p() ? "TETRIS" : "GAME OVER";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			if (tetris.isGameOver2p() == true) {
				if (tetris.isGameOver() == false) {
					g.drawString("1P WIN", CENTER_X - g.getFontMetrics().stringWidth("1P WIN") / 2, 200);
				}
			}
			g.setFont(SMALL_FONT);
			msg = "Press Start to Play" + (tetris.isNewGame() ? "" : " Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			// 보드에 타일을 그림.
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					TileType tile = getTile(x, y);
					if (tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			// 현재 타일을 그림. 아직 게임 보드의 일부가 아니기 때문에 조각의 나머지와 같이 그려 질 수 없음.
			TileType type = tetris.getPieceType2p();
			int pieceCol = tetris.getPieceCol2p();
			int pieceRow = tetris.getPieceRow2p();
			int rotation = tetris.getPieceRotation2p();
			// 보드 위에 조각을 그림
			for (int col = 0; col < type.getDimension(); col++) {
				for (int row = 0; row < type.getDimension(); row++) {
					if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE,
								g);
					}
				}
			}
			// 게임 보드의 배경 눈금을 그림.
			g.setColor(Color.DARK_GRAY);
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		// 게임보드의 테두리를 그림.
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}

	/**
	 * 보드에 타일을 그림.
	 * 
	 * @param tile
	 *            : 그릴 타일의 종류.
	 * @param x
	 *            : x좌표.
	 * @param y
	 *            : y좌표.
	 * @param g
	 *            : 그래픽 오브젝트.
	 */
	private void drawTile(TileType tile, int x, int y, Graphics g) {
		drawTile(tile.getBaseColor(), x, y, g);
	}

	/**
	 * 보드에 타일을 그림.
	 * 
	 * @param base
	 *            : 타일의 기본 색상.
	 * @param x
	 *            : x좌표.
	 * @param y
	 *            : y좌표.
	 * @param g
	 *            : 그래픽 오브젝트.
	 */
	private void drawTile(Color base, int x, int y, Graphics g) {
		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
	}
}
