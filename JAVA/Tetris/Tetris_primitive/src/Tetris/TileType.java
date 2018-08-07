package Tetris;

import java.awt.Color;

public enum TileType {
	/**
	 * ■ ■ ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type0(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), 4, 4, 1, new boolean[][] {
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	/**
	 * ■
	 * ■ ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type1(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	/**
	 *     ■
	 * ■ ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type2(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	/**
	 * ■ ■ 
	 * ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type3(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 2, 2, 2, new boolean[][] {
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	/**
	 *   ■ ■ 
	 * ■ ■   
	 * 모양과 n회전 모양
	 */
	Type4(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	/**
	 *   ■  
	 * ■ ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type5(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, new boolean[][] {
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	/**
	 * ■ ■  
	 *   ■ ■ 
	 * 모양과 n회전 모양
	 */
	Type6(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	}),
	/**
	 * 랜덤값 때문에 생성(true 값)
	 */
	Type7(new Color(0, 0, 0), 1, 1, 1, new boolean[][] {
		{
			true
		},
		{
			true
		},
		{
			true,
		},
		{
			true
		}
	}),
	/**
	 * 랜덤값 때문에 생성(false 값)
	 */
	Type8(new Color(255, 255, 255), 0, 0, 0, new boolean[][] {
		{
			false
		},
		{
			false
		},
		{
			false
		},
		{
			false
		}
	});
	/**
	 * 타일 타입의 기본 색상.
	 */
	private Color baseColor;
	/**
	 * 타일이 나올 열
	 */
	private int spawnCol;
	/**
	 * 타일이 나올 행.
	 */
	private int spawnRow;
	/**
	 * 한 타일의 배열 크기.
	 */
	private int dimension;
	/**
	 * 한 타일의 행의 수. 
	 */
	private int rows;
	/**
	 * 한 타일의 열의 수. 
	 */
	private int cols;
	/**
	 * 한 타일의 배열(각 조각은 각각의 회전에 대한 블럭의 배열을 가지고있음.)
	 */
	private boolean[][] tiles;
	
	/**
	* 새로운 TileType을 생성.
	* @param color : 색 타일의 기본 색상.
	* @param dimension : 차원 타일 배열의 크기.
	* @param cols : 열의 수.
	* @param rows : 행의 수.
	* @param tiles : 타일1~7.
	*/
	private TileType(Color color, int dimension, int cols, int rows, boolean[][] tiles) {
		this.baseColor = color;
		this.dimension = dimension;
		this.tiles = tiles;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}
	
	/**
	* 타입의 기본 색상을 겟.
	* @return baseColor.
	*/
	public Color getBaseColor() {
		return baseColor;
	}
	/**
	 * 타입의 크기를 겟.
	 * @return dimension.
	 */
	public int getDimension() {
		return dimension;
	}
	/**
	 * 타입이 떨어지는 열을 겟.
	 * @return spawnCol.
	 */
	public int getSpawnColumn() {
		return spawnCol;
	}
	/**
	 * 타입이 떨어지는 행을 겟.
	 * @return spawnRow.
	 */
	public int getSpawnRow() {
		return spawnRow;
	}
	/**
	 * 조각의 행의 수를 겟.
	 * @return rows.
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * 조각의 열의 수를 겟.
	 * @return cols.
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * 지정된 좌표와 회전이 포함된 조각일 경우 경우 확인.
	 * @param x : 타일의 x 좌표
	 * @param y : 타일의 y 좌표
	 * @param rotation : 회전을 확인.
	 * @return 타일이 존재하는지 여부.
	 */	
	public boolean isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	
	/**
	* 왼쪽 인셋은 주어진 회전에 대한 배열의 왼쪽 측면 빈 열의 수를 표현.
	* @param rotation : 회전.
	* @return left inset.
	*/
	public int getLeftInset(int rotation) {
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	* 오른쪽 인셋은 주어진 회전에 대한 배열의 오른쪽 측면 빈 열의 수를 표현.
	* @param rotation : 회전.
	* @return right inset.
	*/
	public int getRightInset(int rotation) {
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	
	/**
	* 상단 인셋은 주어진 회전에 대한 배열의 하단 측면 빈 행의 수를 표현.
	* @param rotation : 회전.
	* @return top inset.
	*/
	public int getTopInset(int rotation) {
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	
	/** 
	 * 하단 인셋은 주어진 회전에 대한 배열의 상단 측면 빈 행의 수를 표현. 
	 * @param rotation : 회전.
	 * @return bottom inset.
	 */
	public int getBottomInset(int rotation) {
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
	
}
