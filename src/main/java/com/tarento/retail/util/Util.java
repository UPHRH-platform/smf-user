package com.tarento.retail.util;

import java.util.Random;

public class Util {

	public static String generateOTP() {
		int randomPin = (int) (Math.random() * 900000) + 100000;
		String otp = String.valueOf(randomPin);
		return otp;
	}

	/**
	 * this method is used to generate user session id.
	 *
	 * @param email
	 *            user email id.
	 * @return String session id value.
	 */
	public static String getUniqueSessionId(String email) throws Exception {
		Random random = new Random();
		email = email.replace('.', 'D');
		email = email.replace('@', 'L');
		email = email.replace("com", "se");
		email = email.replace("in", "xp");
		StringBuilder builder = new StringBuilder();
		String str = System.currentTimeMillis() + random.nextInt() + "";
		byte[] data = { '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', '4', 'i', 'j', 'k', '5', 'l', 'm', 'n', 'o', 'p',
				'q', '6', 'A', 'B', 'C', 'D', '9', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', '8', 'M', 'N', 'O', 'P', 'Q',
				'R', '7', 'S', 'T', 't', 'u', 'U', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z', '1', '2', '3' };
		String value = random.nextInt() + "";
		builder.append(value.substring(1, value.length() / 2));
		builder.append(email.substring(3, email.length() / 2));
		builder.append(str.substring(str.length() - 2) + "" + str.substring(0, str.length() - 2));
		builder.append(email.substring(email.length() - 4));
		for (int i = 0; i < 4; i++) {
			builder.append(random.nextInt(data.length));
		}
		builder.append(random.nextInt(random.nextInt(data.length)));
		return builder.toString();
	}

}
