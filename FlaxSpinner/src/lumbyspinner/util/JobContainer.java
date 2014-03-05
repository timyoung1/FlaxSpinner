package lumbyspinner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Aadil Farouk Date: 6/17/13 Time: 3:59 PM
 */

public class JobContainer implements Comparator<Job> {
	private final List<Job> jobList = new ArrayList<Job>();

	public JobContainer(Job[] jobs) {
		submit(jobs);
	}

	public void submit(final Job... jobs) {
		for (Job j : jobs) {
			if (!jobList.contains(j)) {
				jobList.add(j);
			}
		}
		Collections.sort(jobList, this);
	}

	@Override
	public int compare(Job o1, Job o2) {
		return o2.priority() - o1.priority();
	}

	public void revoke(Job j) {
		if (jobList.contains(j)) {
			jobList.remove(j);
		}
	}

	public void clear() {
		jobList.clear();
	}

	public Job get() {
		for (Job j : jobList) {
			if (j.activate()) {
				return j;
			}
		}
		return null;
	}
}