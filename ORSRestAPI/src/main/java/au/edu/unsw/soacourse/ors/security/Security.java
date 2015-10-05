package au.edu.unsw.soacourse.ors.security;

import java.util.Arrays;
import java.util.List;

public enum Security {
	instance;
	
	private static final String ORSKEY = "i-am-ors";
	private static List<String> MANAGERS = Arrays.asList("001-manager");
	private static List<String> REVIEWERS = Arrays.asList("002-reviewer", "003-reviewer", "004-reviewer", "005-reviewer");
	
	private Security() {
		
	}
	
	public boolean isRightKey(String orsKey) {
		return orsKey != null && orsKey.equals(ORSKEY);
	}
	
	public boolean isManager(String shortKey) {
		return shortKey != null && MANAGERS.contains(shortKey);
	}
	
	public boolean isReviewer(String shortKey) {
		return shortKey != null && REVIEWERS.contains(shortKey);
	}
	
	public boolean isInternalUser(String orsKey, String shortKey) {
		return isRightKey(orsKey) && (isManager(shortKey) || isReviewer(shortKey));
	}
}
