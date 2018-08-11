package Tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SidePanel extends JPanel {
	private static final long serialVersionUID = 2181495598854992747L;
	/**
	 * 조각 미리보기의 각 타일의 크기.
	 */
	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
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
	private static final int SQUARE_CENTER_X = 140;
	/**
	 * 미리보기 창의 중앙 Y좌표.
	 */
	private static final int SQUARE_CENTER_Y = 65;
	/**
	 * 미리보기2 창의 중앙 X좌표.
	 */
	private static final int SQUARE2_CENTER_X = 340;
	/**
	 * 미리보기2 창의 중앙 Y좌표.
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
	 * 새로운 사이드 패널을 생성하고 속성을 설정.
	 * 
	 * @param tetris
	 *            Tetris 인스턴스 사용.
	 */
	public SidePanel(Tetris tetris) {
		this.tetris = tetris;

		setPreferredSize(new Dimension(400, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.WHITE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 색상을 설정.
		g.setColor(DRAW_COLOR);
		// 현재 문자열의 y 좌표를 저장.
		int offset;
		// tetris 삽입.
		g.setFont(LARGE_FONT);
		g.drawString("TETRIS", 175, offset = 220);
		// Control 삽입.
		g.setFont(SMALL_FONT);
		g.drawString("Control", 20, offset = 250);
		g.drawString("up:", 20, offset += 15);
		g.drawString("left:", 20, offset += 15);
		g.drawString("right:", 20, offset += 15);
		g.drawString("down:", 20, offset += 15);
		g.drawString("fastdown:", 20, offset += 15);
		g.drawString("change:", 20, offset += 15);
		// 1P
		g.setFont(SMALL_FONT);
		g.drawString("1P", 100, offset = 250);
		g.drawString("W", 100, offset += 15);
		g.drawString("A", 100, offset += 15);
		g.drawString("D", 100, offset += 15);
		g.drawString("S", 100, offset += 15);
		g.drawString("space", 100, offset += 15);
		g.drawString("ctrl", 100, offset += 15);
		// 2P
		g.setFont(SMALL_FONT);
		g.drawString("2P", 280, offset = 250);
		g.drawString("K_UP", 280, offset += 15);
		g.drawString("K_LEFT", 280, offset += 15);
		g.drawString("K_RIGHT", 280, offset += 15);
		g.drawString("K_DOWN", 280, offset += 15);
		g.drawString("shift", 280, offset += 15);
		g.drawString("L", 280, offset += 15);
		// info 삽입.
		g.setFont(SMALL_FONT);
		g.drawString("info", 20, offset = 370);
		// 1P
		g.drawString("1P", 80, offset = 370);
		g.drawString("Level: " + tetris.getLevel() + " lv", 80, offset += 25);
		g.drawString("Score: " + tetris.getcurrentScore(), 80, offset += 25);
		g.setFont(SMALL_FONT);
		// 2P
		g.drawString("2P", 280, offset = 370);
		g.drawString("Level: " + tetris.getLevel2p() + " lv", 280, offset += 25);
		g.drawString("Score: " + tetris.getcurrentScore2p(), 280, offset += 25);
		// 최고 점수
		if(ScorePanel.Max_level>=tetris.getcurrentScore()&&ScorePanel.Max_level>tetris.getcurrentScore2p()){
			g.drawString("Best Score: " + ScorePanel.Max_level, 160, offset += 25);
		}else if(ScorePanel.Max_level<tetris.getcurrentScore()||ScorePanel.Max_level<tetris.getcurrentScore2p()){
			if(tetris.getcurrentScore()>=tetris.getcurrentScore2p()){
				g.drawString("Best Score: " + tetris.getcurrentScore(), 160, offset += 25);
			}else if(tetris.getcurrentScore2p()>tetris.getcurrentScore()){
				g.drawString("Best Score: " + tetris.getcurrentScore2p(), 160, offset += 25);
			}			
		}		
		// 1P의 다음 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("Next:", 20, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 1P의 다다음 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("After Next:", 20, 140);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 2P의 다음 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("Next:", 220, 70);
		g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 2P의 다다음 미리보기 창
		g.setFont(SMALL_FONT);
		g.drawString("After Next:", 220, 140);
		g.drawRect(SQUARE2_CENTER_X - SQUARE_SIZE, SQUARE2_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 4);
		// 1P의 다음 조각의 미리보기를 그림.
		TileType type1 = tetris.getNextPieceType();
		if (!tetris.isGameOver() && type1 != null) {
			// 현재 조각의 크기를 가져옴
			int dimension = type1.getDimension();
			// 조각의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE_CENTER_Y - TILE_SIZE;
			// 미리보기의 인셋을 가져옴. 기본 회전으로 0.
			int top = type1.getTopInset(0);
			int left = type1.getLeftInset(0);
			// 미리보기에 조각과 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type1.isTile(col, row, 0)) {
						drawTile(type1, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
		// 1P의 다다음 조각의 미리보기를 그림.
		TileType type2 = tetris.getNextPieceType2();
		if (!tetris.isGameOver() && type2 != null) {
			// 현재 조각의 크기를 가져옴
			int dimension = type2.getDimension();
			// 조각의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE_CENTER_Y + TILE_SIZE * 5;
			// 미리보기의 인셋을 가져옴. 기본 회전으로 0.
			int top = type2.getTopInset(0);
			int left = type2.getLeftInset(0);
			// 미리보기에 조각과 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type2.isTile(col, row, 0)) {
						drawTile(type2, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
		// 2P의 다음 조각의 미리보기를 그림.
		TileType type3 = tetris.getNextPieceType2p();
		if (!tetris.isGameOver2p() && type3 != null) {
			// 현재 조각의 크기를 가져옴
			int dimension = type3.getDimension();
			// 조각의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE2_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE2_CENTER_Y - TILE_SIZE;
			// 미리보기의 인셋을 가져옴. 기본 회전으로 0.
			int top = type3.getTopInset(0);
			int left = type3.getLeftInset(0);
			// 미리보기에 조각과 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type3.isTile(col, row, 0)) {
						drawTile(type3, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
		// 2P의 다다음 조각의 미리보기를 그림.
		TileType type4 = tetris.getNextPieceType22p();
		if (!tetris.isGameOver2p() && type4 != null) {
			// 현재 조각의 크기를 가져옴
			int dimension = type4.getDimension();
			// 조각의 왼쪽 상단 모서리 (원점)를 계산.
			int startX = SQUARE2_CENTER_X - TILE_SIZE * 2;
			int startY = SQUARE2_CENTER_Y + TILE_SIZE * 5;
			// 미리보기의 인셋을 가져옴. 기본 회전으로 0.
			int top = type4.getTopInset(0);
			int left = type4.getLeftInset(0);
			// 미리보기에 조각과 타일을 그림.
			for (int row = 0; row < dimension; row++) {
				for (int col = 0; col < dimension; col++) {
					if (type4.isTile(col, row, 0)) {
						drawTile(type4, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
	}

	/**
	 * 미리보기 창에 타일을 그림.
	 * 
	 * @param type
	 *            : 그릴 타일의 타입.
	 * @param x
	 *            : 타일의 x 좌표.
	 * @param y
	 *            : 타일의 y 좌표.
	 * @param g
	 *            : 그래픽 오브젝트.
	 */
	private void drawTile(TileType type, int x, int y, Graphics g) {
		// 기본 색상과 전체 타일을 입력
		g.setColor(type.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		g.setColor(DRAW_COLOR);
		for (int x1 = 0; x1 < TILE_COL; x1++) {
			for (int y1 = 0; y1 < TILE_ROW; y1++) {
				g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, TILE_COL * TILE_SIZE,
						y1 * TILE_SIZE);
				g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, x1 * TILE_SIZE,
						TILE_ROW * TILE_SIZE);
			}
		}
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
