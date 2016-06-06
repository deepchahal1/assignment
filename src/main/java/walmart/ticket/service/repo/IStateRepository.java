package walmart.ticket.service.repo;


import java.util.Map;

/**
 * to persist state.
 * @author deep
 *
 * @param <T>
 * @param <E>
 */
public interface IStateRepository<T, E> {
    /**
     * persist state
     * @param seatHoldState
     * @param value
     */
	void persist(final T seatHoldState, final E value);
	/**
	 * load initial state.
	 * @return
	 */
	Map<T,E> loadInitialState();
}
