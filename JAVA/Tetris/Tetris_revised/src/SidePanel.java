
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SidePanel extends JPanel {
	private static final long serialVersionUID = 2181495598854992747L;
	/**
	 * 타일 미리보기 창의 크기.
	 */
	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1; // BoardPanel 사이즈 /2
	/**
	 * 미리보기 창의 행과 열의 수.
	 */
	private static final int TILE_COUNT = 6;
	/**
	 * 미리보기 창의 행의 수.
	 */
	private static final int TILE_ROW = 12;
	/**
	 * 미리보기 창의 열의 수.
	 */
	private static final int TILE_COL = 6;
	/**
	 * 미리보기 창의 중앙 X좌표.
	 */
	private static final int SQUARE_CENTER_X = 65;
	/**
	 * 미리보기 창의 중앙 Y좌표.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	/**
	 * 미리보기2P 창의 중앙 X좌표.
	 */
	private static final int SQUARE2_CENTER_X = 340;
	/**
	 * 미리보기2P 창의 중앙 Y좌표.
	 */
	private static final int SQUARE2_CENTER_Y = 65;
	/**
	 * 미리보기 창의 크기.
	 */
	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);
	/**
	 * 작은 폰트.
	 */
	private static final Font SMALL_FONT = new Font("굴림", Font.BOLD, 11);
	/**
	 * 큰 폰트.
	 */
	private static final Font LARGE_FONT = new Font("굴림", Font.BOLD, 16);
	/**
	 * 텍스트와 미리보기 창의 색.
	 */
	private static final Color DRAW_COLOR = new Color(0, 0, 0);
	/**
	 * Tetris 인스턴스.
	 */
	private Tetris tetris;

	/**
	 * 새 사이드 패널을 생성하고 크기와 색상 설정.
	 * 
	 * @param tetris Tetris 인스턴스 사용.
	 */
	public SidePanel(Tetris tetris) {
		this.tetris = tetris;

		setPreferredSize(new Dimension(400, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.WHITE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		String s1 = null;
		// 색상을 설정.
		g.setColor(DRAW_COLOR);

		// tetris 삽입.
		g.setFont(LARGE_FONT);
		g.drawString("TETRIS", 175, 210);

		// Control 삽입.
		g.setFont(SMALL_FONT);
		g.drawString(s1 = "---------------------------------------------------",
				(400 - (int) g.getFontMetrics().stringWidth(s1)) / 2, 248);
		String[] stringCArr = { "", "1P", "W", "A", "D", "S", "space", "ctrl", "", "Control", "up", "left", "right",
				"down", "fastdown", "change", "", "2P", "K_UP", "K_LEFT", "K_RIGHT", "K_DOWN", "shift", "L", "" };

		int i = 1;
		for (int x = 200; x <= 600; x += 200, i++)
			for (int y = 240; i % 8 != 0; i++, y += 15) {
				g.drawString(stringCArr[i], (x - (int) g.getFontMetrics().stringWidth(stringCArr[i])) / 2, y);
			}

		// Info 삽입.
		g.drawString(s1 = "---------------------------------------------------",
				(400 - (int) g.getFontMetrics().stringWidth(s1)) / 2, 378);
		String[] stringIArr = { "", "1P", tetris.getLevel() + "", tetris.getcurrentScore() + "", "", "Info", "level",
				"score", "", "2P", tetris.getLevel2p() + "", tetris.getcurrentScore2p() + "", "" };
		i = 1;
		for (int x = 200; x <= 600; x += 200, i++)
			for (int y = 370; i % 4 != 0; i++, y += 15) {
				g.drawString(stringIArr[i], (x - (int) g.getFontMetrics().stringWidth(stringIArr[i])) / 2, y);
			}

		// 최고 점수
		if (ScorePanel.Max_level >= tetris.getcurrentScore() && ScorePanel.Max_level >= tetris.getcurrentScore2p()) {
			g.drawString("Best Score: " + ScorePanel.Max_level, 150, 425);
		} else if (ScorePanel.Max_level < tetris.getcurrentScore()
				|| ScorePanel.Max_level < tetris.getcurrentScore2p()) {
			if (tetris.getcurrentScore() >= tetris.getcurrentScore2p()) {
				g.drawString("Best Score: " + tetris.getcurrentScore(), 150, 425);
			} else if (tetris.getcurrentScore2p() > tetris.getcurrentScore()) {
				g.drawString("Best Score: " + tetris.getcurrentScore2p(), 150, 425);
			}
		}
		// 1P의 다음 타일 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("Next", 110, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 1P의 다다음 타일 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("After Next", 110, 140);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);

		// 2P의 다음 타일 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("Next", 270, 70);
		g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 2P의 다다음 타일 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("After Next", 230, 140);
		g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);

		// 1P의 다음 타일 미리보기 그림.
		TileType type1 = tetris.getNextPieceType();
		if (!tetris.isGameOver() && type1 != null) {
			// 현재 타일의 크기를 가져옴
			int dimension = type1.getDimension();
			// 타일의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE_CENTER_Y - TILE_SIZE;
			// 타일 미리보기창에 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type1.isTile(col, row, 0)) {
						drawTile(type1, startX + (col * TILE_SIZE), startY + (row * TILE_SIZE), g);
					}
				}
			}
		}
		// 1P의 다다음 타일 미리보기를 그림.
		TileType type2 = tetris.getNextPieceType2();
		if (!tetris.isGameOver() && type2 != null) {
			// 현재 타일의 크기를 가져옴
			int dimension = type2.getDimension();
			// 타일의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE_CENTER_Y + TILE_SIZE * 5;
			// 타일 미리보기 창에 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type2.isTile(col, row, 0)) {
						drawTile(type2, startX + (col * TILE_SIZE), startY + (row * TILE_SIZE), g);
					}
				}
			}
		}
		// 2P의 다음 타일 미리보기를 그림.
		TileType type3 = tetris.getNextPieceType2p();
		if (!tetris.isGameOver2p() && type3 != null) {
			// 현재 타일의 크기를 가져옴
			int dimension = type3.getDimension();
			// 타일의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE2_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE2_CENTER_Y - TILE_SIZE;
			// 타일 미리보기 창에 타일을 그림
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type3.isTile(col, row, 0)) {
						drawTile(type3, startX + (col * TILE_SIZE), startY + (row * TILE_SIZE), g);
					}
				}
			}
		}
		// 2P의 다다음 타일 미리보기를 그림.
		TileType type4 = tetris.getNextPieceType22p();
		if (!tetris.isGameOver2p() && type4 != null) {
			// 현재 타일의 크기를 가져옴
			int dimension = type4.getDimension();
			// 타일의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE2_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE2_CENTER_Y + TILE_SIZE * 5;
			// 타일 미리보기 창에 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type4.isTile(col, row, 0)) {
						drawTile(type4, startX + (col * TILE_SIZE), startY + (row * TILE_SIZE), g);
					}
				}
			}
		}
	}

	/**
	 * 타일 미리보기 창에 타일을 그림.
	 * 
	 * @param type : 그릴 타일의 타입.
	 * @param x    : 타일의 x 좌표.
	 * @param y    : 타일의 y 좌표.
	 * @param g    : 그래픽 오브젝트.
	 */
	private void drawTile(TileType type, int x, int y, Graphics g) {
		// 미리보기 타일을 그림.
		g.setColor(type.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		g.setColor(DRAW_COLOR);
		// 미리보기 창을 그림.
		for (int x1 = 0; x1 < TILE_COL; x1++) {
			for (int y1 = 0; y1 < TILE_ROW; y1++) {
				g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, TILE_COL * TILE_SIZE,
						y1 * TILE_SIZE);
				g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, x1 * TILE_SIZE,
						TILE_ROW * TILE_SIZE);
			}
		}
		// 미리보기 창을 그림(2P).
		for (int x1 = 0; x1 < TILE_COL; x1++) {
			for (int y1 = 0; y1 < TILE_ROW; y1++) {
				g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, TILE_COL * TILE_SIZE,
						y1 * TILE_SIZE);
				g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, x1 * TILE_SIZE,
						TILE_ROW * TILE_SIZE);
			}
		}
	}
}
