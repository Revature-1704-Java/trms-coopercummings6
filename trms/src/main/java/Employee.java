import java.io.Serializable;

public class Employee implements Serializable
{
	String name;
	double claimsAmountRemaining;
	String eMail;
	String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getClaimsAmountRemaining() {
		return claimsAmountRemaining;
	}
	public void setClaimsAmountRemaining(double claimsAmountRemaining) {
		this.claimsAmountRemaining = claimsAmountRemaining;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
