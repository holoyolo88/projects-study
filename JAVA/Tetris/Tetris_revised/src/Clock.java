
public class Clock {
	/**
	 * 한 사이클을 구성하는 밀리 초.
	 */
	private float millisPerCycle;
	/**
	 * (델타 시간을 계산하기 위해 사용 된)시계가 업데이트 된 마지막 시간.
	 */
	private long lastUpdate;
	/**
	 * 경과된 사이클의 수.
	 */
	private int elapsedCycles;
	/**
	 * 다음 경과 주기 대한 초과 시간.
	 */
	private float excessCycles;
	/**
	 * 일시 정지 여부.
	 */
	private boolean isPaused;

	/**
	 * 새로운 시계를 만들고 초당 사이클 설정.
	 * 
	 * @param cyclesPerSecond : 초당 경과된 사이클 수.
	 */
	public Clock(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}

	/**
	 * 초당 경과된 사이클의 수를 설정.
	 * 
	 * @param cyclesPerSecond : 초당 사이클의 수.
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}

	/**
	 * 시계 통계를 재설정.
	 */
	public void reset() {
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = getCurrentTime();
		this.isPaused = false;
	}

	/**
	 * 시계 통계를 업데이트.
	 */
	public void update() {
		// 현재 시간을 얻고 델타 시간을 계산.
		long currUpdate = getCurrentTime();
		float delta = (float) (currUpdate - lastUpdate) + excessCycles;

		// 일시 중지하지 않으면 경과되고 초과된 수를 업데이트.
		if (!isPaused) {
			this.elapsedCycles += (int) Math.floor(delta / millisPerCycle);
			this.excessCycles = delta % millisPerCycle;
		}
		// 마지막 업데이트 시간을 다음 업데이트 주기로 설정.
		this.lastUpdate = currUpdate;
	}

	/**
	 * 일시 중지 된 동안 시계는 업데이트 되지않음.
	 * 
	 * @param paused : 시계가 정지 되었는지 여부.
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}

	/**
	 * 시계가 일시정지 되었는지 확인.
	 * 
	 * @return 시계의 일시 정지 여부.
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * 사이클이 시계에 대해 경과 한 경우를 확인. 그렇다면 경과 사이클의 수는 1만큼 감소.
	 * 
	 * @return 사이클이 경과 하였는지의 여부.
	 */
	public boolean hasElapsedCycle() {
		if (elapsedCycles > 0) {
			this.elapsedCycles--;
			return true;
		}
		return false;
	}

	/**
	 * 사이클이 시계에 대해 경과 한 경우를 확인. 경과 된 사이클의 개수가 0보다 큰 경우와 다르게 사이클의 수는 감소되지 않음.
	 * 
	 * @return 사이클이 경과 하였는지의 여부.
	 */
	public boolean peekElapsedCycle() {
		return (elapsedCycles > 0);
	}

	/**
	 * 시스템을 이용해 현재시간을 밀리초로 계산.
	 * 
	 * @return (밀리초) 현재시간.
	 */
	private static final long getCurrentTime() {
		return (System.nanoTime() / 1000000L);
	}

}
