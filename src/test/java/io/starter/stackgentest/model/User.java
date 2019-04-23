package io.starter.stackgentest.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.starter.ignite.security.securefield.SecureField;
import io.swagger.annotations.ApiModelProperty;

/**
 User
 */
public class User implements io.starter.ignite.model.DataModelObject {

	@JsonProperty("id")
	public Long				id				= null;

	// add the Starter Ignite Annotations
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			strength = 128)
	@JsonProperty("fingerprint")
	public String			fingerprint		= null;

	// add the Starter Ignite Annotations
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			strength = 256)
	@JsonProperty("firstName")
	public String			firstName		= null;

	// add the Starter Ignite Annotations
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			strength = 128)
	@JsonProperty("lastName")
	public String			lastName		= null;

	// add the Starter Ignite Annotations
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			strength = 256)
	@JsonProperty("governmentId")
	public String			governmentId	= null;

	@JsonProperty("userName")
	public String			userName		= null;

	// EXAMPLE HASHED IMPLEMENTATION
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			type = SecureField.Type.HASHED,
			strength = 20)
	@JsonProperty("password")
	public String			password		= null;

	@JsonProperty("homePage")
	public String			homePage		= null;

	// add the Starter Ignite Annotations
	@io.starter.ignite.security.securefield.SecureField(
			enabled = true,
			strength = 256)
	@JsonProperty("email")
	public String			email			= null;

	@JsonProperty("social")
	public String			social			= null;

	@JsonProperty("keyVersion")
	public Long				keyVersion		= null;

	@JsonProperty("keySpec")
	public String			keySpec			= "dev";

	@io.starter.ignite.model.DataField()
	@JsonProperty("ownerId")
	public Long				ownerId			= null;

	@JsonProperty("createdDate")
	public OffsetDateTime	createdDate		= null;

	@JsonProperty("modifiedDate")
	public OffsetDateTime	modifiedDate	= null;

	public User id(Long id) {
		this.id = id;
		return this;
	}

	/**
	* Primary Key for Object (generated)
	* @return id
	**/

	@ApiModelProperty(value = "Primary Key for Object (generated)")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User fingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
		return this;
	}

	/**
	* Get fingerprint
	* @return fingerprint
	**/

	@ApiModelProperty(example = "DK$DFSJaraDD", value = "")
	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public User firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	* first name of person (encrypted)
	* @return firstName
	**/

	@ApiModelProperty(
			example = "Karena",
			value = "first name of person (encrypted)")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public User lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	* last name of person (encrypted)
	* @return lastName
	**/

	@ApiModelProperty(
			example = "Jones",
			value = "last name of person (encrypted)")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User governmentId(String governmentId) {
		this.governmentId = governmentId;
		return this;
	}

	/**
	* a 10 digit government ID (encrypted)
	* @return governmentId
	**/

	@Size(min = 10, max = 10)
	@ApiModelProperty(
			example = "1112233334",
			minLength = 10,
			maxLength = 10,
			value = "a 10 digit government ID (encrypted)")
	public String getGovernmentId() {
		return governmentId;
	}

	public void setGovernmentId(String governmentId) {
		this.governmentId = governmentId;
	}

	public User userName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	* Get userName
	* @return userName
	**/

	@ApiModelProperty(example = "Sparky", value = "")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User password(String password) {
		this.password = password;
		return this;
	}

	/**
	* Get password
	* @return password
	**/
	@NotNull

	@Size(min = 10)
	@ApiModelProperty(
			example = "HardToGuess1980",
			minLength = 10,
			required = true,
			value = "")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User homePage(String homePage) {
		this.homePage = homePage;
		return this;
	}

	/**
	* Get homePage
	* @return homePage
	**/

	@ApiModelProperty(example = "https://www.acme-corp.com", value = "")
	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public User email(String email) {
		this.email = email;
		return this;
	}

	/**
	* the main email address for the user  (encrypted)
	* @return email
	**/
	@NotNull

	@ApiModelProperty(
			example = "wiley.coyote@acme-corp.com",
			required = true,
			value = "the main email address for the user  (encrypted)")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User social(String social) {
		this.social = social;
		return this;
	}

	/**
	* Get social
	* @return social
	**/

	@ApiModelProperty(example = "https://twitter.com/StackGen", value = "")
	public String getSocial() {
		return social;
	}

	public void setSocial(String social) {
		this.social = social;
	}

	public User keyVersion(Long keyVersion) {
		this.keyVersion = keyVersion;
		return this;
	}

	/**
	* The version of the SecureField key used to crypt this row (generated)
	* @return keyVersion
	**/

	@ApiModelProperty(
			value = "The version of the SecureField key used to crypt this row (generated)")
	public Long getKeyVersion() {
		return keyVersion;
	}

	public void setKeyVersion(Long keyVersion) {
		this.keyVersion = keyVersion;
	}

	public User keySpec(String keySpec) {
		this.keySpec = keySpec;
		return this;
	}

	/**
	* The spec of the SecureField key used to crypt this row (generated)
	* @return keySpec
	**/

	@ApiModelProperty(
			example = "{keyOwner:111, keySource:'session | system'}",
			value = "The spec of the SecureField key used to crypt this row (generated)")
	public String getKeySpec() {
		return keySpec;
	}

	public void setKeySpec(String keySpec) {
		this.keySpec = keySpec;
	}

	public User ownerId(Long ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	/**
	* The ID of the user that owns this data (generated)
	* @return ownerId
	**/

	@ApiModelProperty(
			value = "The ID of the user that owns this data (generated)")
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public User createdDate(OffsetDateTime createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	/**
	* The created date for this record/object (generated)
	* @return createdDate
	**/

	@Valid

	@ApiModelProperty(
			value = "The created date for this record/object (generated)")
	public OffsetDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(OffsetDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public User modifiedDate(OffsetDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
		return this;
	}

	/**
	* The last-modified date for this record/object (generated)
	* @return modifiedDate
	**/

	@Valid

	@ApiModelProperty(
			value = "The last-modified date for this record/object (generated)")
	public OffsetDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(OffsetDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(this.id, user.id)
				&& Objects.equals(this.fingerprint, user.fingerprint)
				&& Objects.equals(this.firstName, user.firstName)
				&& Objects.equals(this.lastName, user.lastName)
				&& Objects.equals(this.governmentId, user.governmentId)
				&& Objects.equals(this.userName, user.userName)
				&& Objects.equals(this.password, user.password)
				&& Objects.equals(this.homePage, user.homePage)
				&& Objects.equals(this.email, user.email)
				&& Objects.equals(this.social, user.social)
				&& Objects.equals(this.keyVersion, user.keyVersion)
				&& Objects.equals(this.keySpec, user.keySpec)
				&& Objects.equals(this.ownerId, user.ownerId)
				&& Objects.equals(this.createdDate, user.createdDate)
				&& Objects.equals(this.modifiedDate, user.modifiedDate);
	}

	@Override
	public int hashCode() {
		return Objects
				.hash(id, fingerprint, firstName, lastName, governmentId, userName, password, homePage, email, social, keyVersion, keySpec, ownerId, createdDate, modifiedDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class User {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    fingerprint: ").append(toIndentedString(fingerprint))
				.append("\n");
		sb.append("    firstName: ").append(toIndentedString(firstName))
				.append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName))
				.append("\n");
		sb.append("    governmentId: ").append(toIndentedString(governmentId))
				.append("\n");
		sb.append("    userName: ").append(toIndentedString(userName))
				.append("\n");
		sb.append("    password: ").append(toIndentedString(password))
				.append("\n");
		sb.append("    homePage: ").append(toIndentedString(homePage))
				.append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    social: ").append(toIndentedString(social)).append("\n");
		sb.append("    keyVersion: ").append(toIndentedString(keyVersion))
				.append("\n");
		sb.append("    keySpec: ").append(toIndentedString(keySpec))
				.append("\n");
		sb.append("    ownerId: ").append(toIndentedString(ownerId))
				.append("\n");
		sb.append("    createdDate: ").append(toIndentedString(createdDate))
				.append("\n");
		sb.append("    modifiedDate: ").append(toIndentedString(modifiedDate))
				.append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	* Starter Ignite JSON method
	*/
	@Override
	public String toJSON() {
		String ret = new org.json.JSONObject(this).toString();
		ret = ret.replace("\\\"", "\"");
		ret = ret.replace("\"}\"", "\"}");
		return ret;
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
