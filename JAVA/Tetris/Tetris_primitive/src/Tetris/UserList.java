package Tetris;

public class UserList implements Comparable<UserList>{
	String id;
	int score;
	int level;
	/**
	 * 랭킹 ArrayList.
	 * 
	 * @param name
	 *            : 전달받은 닉네임.
	 * @param lv
	 *            : 전달받은 현재 레벨.
	 * @param sc
	 *            : 전달받은 현재 점수.
	 */
	public UserList(String name, int lv ,int sc){
		id = name;
		level = lv;
		score = sc;		
	}
	/**
	 * ID를 겟.
	 * 
	 * @return ID.
	 */
	public String getID(){
		return id;
	}
	/**
	 * Score를 겟.
	 * 
	 * @return Score.
	 */
	public int getScore(){
		return score;
	}
	/**
	 * Level을 겟.
	 * 
	 * @return Level.
	 */
	public int getlv(){
		return level;
	}	
	
	/**
	 * UserList 정렬.
	 * 
	 * @return 점수(오름차순)로 정렬.
	 */
	public int compareTo(UserList r2) {
		return r2.score - this.score;
	}
}
