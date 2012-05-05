package org.bpel4chor.utils;


/**
 * WSUIdGenerator generates a system wide unique wsu:id string whose length is
 * pre-set and consists of number and character.
 * 
 * @since Jun 16, 2012
 * @author Daojun Cui
 */
public class WSUIDGenerator {

	/** how long the id */
	private final static int idLength = 4;

	/** counter for limit check */
	private static int counter = 0;

	/**
	 * Base of randomised characters [0-9, a-z, A-Z], it provides > [default]
	 * 62^4>12000000 possible ids. If more ids are desired, just set the
	 * idLength to higher number.
	 */
	private static char[] randBase = new char[62];

	private static int randBaseSize = randBase.length;

	/** Allow only 0.01 of the maximum size of the possible ids */
	private static int maxSize = (int) (Math.pow(randBaseSize, idLength) * 0.01);

	/** guarantee only (0.01 * maxSize) ids are unique */
	private static String[] usedIds = new String[maxSize];

	static {
		// init random base
		char num = '0';
		for (int i = 0; i < 10; i++) {
			randBase[i] = num++;
		}
		char a = 'a';
		for (int i = 10; i < 36; i++) {
			randBase[i] = a++;
		}
		char A = 'A';
		for (int i = 36; i < 62; i++) {
			randBase[i] = A++;
		}
	}

	/**
	 * Get a system wide id with the length given
	 * 
	 * @param lengthMinFour
	 * @return
	 */
	public static String getId() {

		if (isOutOfLimit()) {
			reset();
			throw new RuntimeException("Maximum size reached, reset!");

		}

		StringBuffer sb = null;
		do {
			sb = new StringBuffer();
			for (int i = 0; i < idLength; i++) {
				char c = randBase[(int) (Math.random() * randBaseSize)];
				sb.append(c);
			}
		} while (exists(counter, sb.toString()));
		usedIds[counter] = sb.toString();
		counter++;

		return sb.toString();
	}

	/**
	 * Whether the id has already existed
	 * 
	 * @param counter
	 *            How many id there are already
	 * @param id
	 *            The new id
	 * @return
	 */
	private static boolean exists(int counter, String id) {
		for (int i = 0; i < counter; i++) {
			String usedId = usedIds[i];
			if (usedId == null)
				throw new IllegalStateException();
			if (usedId.equals(id))
				return true;
		}
		return false;
	}

	/**
	 * Test whether the ids whose length as same as the given one has reach the
	 * limit.
	 * 
	 * @return
	 */
	private static boolean isOutOfLimit() {
		return counter < maxSize ? false : true;
	}

	public static int idLength() {
		return idLength;
	}

	public static int randBaseSize() {
		return randBase.length;
	}

	public static int maxSize() {
		return maxSize;
	}

	public static int usedSize() {
		return counter;
	}

	public static void reset() {
		counter = 0;
		for (int i = 0; i < maxSize; i++) {
			usedIds[i] = null;
		}
	}
}
