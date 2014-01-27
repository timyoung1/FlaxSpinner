package lumbyspinner.data;

public enum Constantss {
	Flax(1779), BowString(1777), SpinningWheel(36970), groundfloorstairs(36773), secondfloorstairs(
			36774), thirdfloorstairs(36775), bankladder(36772);

	final int Id;

	Constantss(final int id) {
		this.Id = id;
	}

	public int getId() {
		return Id;
	}
}