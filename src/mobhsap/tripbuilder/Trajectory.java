package sethe.tripbuilder;

public class Trajectory {
		private String id;
		private Stop begin;
		private Stop end;

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Stop getBegin() {
			return begin;
		}
		public void setBegin(Stop begin) {
			this.begin = begin;
		}
		public Stop getEnd() {
			return end;
		}
		public void setEnd(Stop end) {
			this.end = end;
		}
}
