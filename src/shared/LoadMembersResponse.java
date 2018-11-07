package shared;

import java.util.List;

public class LoadMembersResponse extends Response{
	private List<String> memberList;
	
	public LoadMembersResponse(List<String> memberList) {
		this.setMemberList(memberList);
	}

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

}
