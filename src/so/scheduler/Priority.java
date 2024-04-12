package so.scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import so.SubProcess;
import so.SystemCallType;
import so.SystemOperation;
import so.cpu.Core;
import so.process.Process;

public class Priority extends Scheduler {
	
	private PriorityQueue<SubProcess> queue;
	
	private int order;
	
	
	public Priority(int order) {
		super();
		this.order = order;
		
		Comparator<SubProcess> comparatorSp = new Comparator<SubProcess>() {

			@Override
			public int compare(SubProcess sp1, SubProcess sp2) {
			
					return sp1.getTimeToExecute() >= sp2.getTimeToExecute() 
							? 1 : -1;
					
			}	
			
		}; 
		this.queue = new PriorityQueue<>(comparatorSp);
	}

	

	@Override
	public void execute(Process process) {
		List<SubProcess> subProcess = SystemOperation.systemCall(SystemCallType.READ_PROCESS, process);
		
	}

	@Override
	public void finish(Process process) {
		List<SubProcess> listSubProcess = SystemOperation.systemCall(SystemCallType.READ_PROCESS, process);
		for (SubProcess subProcess : listSubProcess) {
			this.queue.add(subProcess);
		}
		while (!this.queue.isEmpty()) {
			Core[] cores = this.getCpuManager().getCores();
			for(int i = 0; i < cores.length; i++) {
				if (cores[i].getActuallySubProcess() == null) {
					this.getCpuManager().registerProcess(i, this.queue.poll());
 				}
			}
		}
		
		
	}



	@Override
	public boolean isEmpty(Process process) {
		// TODO Auto-generated method stub
		return false;
	}

}
