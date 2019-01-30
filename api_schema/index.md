---
title: TradeAutomator API
language_tabs:
  - javascript: JavaScript
  - java: Java
  - javascript: JavaScript
  - java: Java
toc_footers:
  - >-
    <a href="https://starterinc.github.io/Ignite/">Find out more about Starter
    Ignite</a>
includes: []
search: true
highlight_theme: darkula
headingLevel: 2

---

<h1 id="tradeautomator-api">TradeAutomator API v1.0.0</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.

This is the TradeAutomator API

Base URLs:

<a href="http://apicloud.co/tos/">Terms of service</a>
Email: <a href="mailto:info@apicloud.co">Support</a> 
License: <a href="https://www.gnu.org/licenses/agpl-3.0.html">AGPL 3.0</a>

# Authentication

- oAuth2 authentication. 

    - Flow: implicit
    - Authorization URL = [http://apicloud.co/oauth/dialog](http://apicloud.co/oauth/dialog)

|Scope|Scope Description|
|---|---|
|write:items|modify items in your account|
|read:items|read your items|

* API Key (api_key)
    - Parameter Name: **api_key**, in: header. 

<h1 id="tradeautomator-api-account">account</h1>

## Add a new account to the system

<a id="opIdaddaccount"></a>

> Code samples

```javascript
var headers = {
  'Content-Type':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account',
  method: 'post',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Content-Type':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account',
  method: 'post',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`POST /account`

> Body parameter

```json
{
  "id": 0,
  "balance": 0,
  "institutionName": "string",
  "accountNumber": "string",
  "routingNumber": "string",
  "modifiedDate": "2018-12-30T04:46:06Z",
  "status": "ready",
  "verified": false
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Account>
  <id>0</id>
  <balance>0</balance>
  <institutionName>string</institutionName>
  <accountNumber>string</accountNumber>
  <routingNumber>string</routingNumber>
  <modifiedDate>2018-12-30T04:46:06Z</modifiedDate>
  <status>ready</status>
  <verified>false</verified>
</Account>
```

<h3 id="add-a-new-account-to-the-system-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[Account](#schemaaccount)|true|account object that needs to be added to the system|

<h3 id="add-a-new-account-to-the-system-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|405|[Method Not Allowed](https://tools.ietf.org/html/rfc7231#section-6.5.5)|Invalid input|None|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
automator_auth ( Scopes: write:items read:items )
</aside>

## Update an existing account

<a id="opIdupdateaccount"></a>

> Code samples

```javascript
var headers = {
  'Content-Type':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account',
  method: 'put',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("PUT");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Content-Type':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account',
  method: 'put',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("PUT");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`PUT /account`

> Body parameter

```json
{
  "id": 0,
  "balance": 0,
  "institutionName": "string",
  "accountNumber": "string",
  "routingNumber": "string",
  "modifiedDate": "2018-12-30T04:46:06Z",
  "status": "ready",
  "verified": false
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Account>
  <id>0</id>
  <balance>0</balance>
  <institutionName>string</institutionName>
  <accountNumber>string</accountNumber>
  <routingNumber>string</routingNumber>
  <modifiedDate>2018-12-30T04:46:06Z</modifiedDate>
  <status>ready</status>
  <verified>false</verified>
</Account>
```

<h3 id="update-an-existing-account-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[Account](#schemaaccount)|true|account object that needs to be updated in the system|

<h3 id="update-an-existing-account-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid ID supplied|None|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Account not found|None|
|405|[Method Not Allowed](https://tools.ietf.org/html/rfc7231#section-6.5.5)|Validation exception|None|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
automator_auth ( Scopes: write:items read:items )
</aside>

## Finds accounts by ID

<a id="opIdlist"></a>

> Code samples

```javascript
var headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account/list',
  method: 'get',
  data: '?id=string',
  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account/list?id=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'

};

$.ajax({
  url: '/account/list',
  method: 'get',
  data: '?id=string',
  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/account/list?id=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`GET /account/list`

Multiple ID values can be provided with comma separated strings

<h3 id="finds-accounts-by-id-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|id|query|string|true|ID values that need to be considered for filter|

> Example responses

> 200 Response

```json
[
  {
    "id": 0,
    "balance": 0,
    "institutionName": "string",
    "accountNumber": "string",
    "routingNumber": "string",
    "modifiedDate": "2018-12-30T04:46:06Z",
    "status": "ready",
    "verified": false
  }
]
```

<h3 id="finds-accounts-by-id-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid id value|None|

<h3 id="finds-accounts-by-id-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Account](#schemaaccount)]|false|none|none|
|» id|integer(int64)|false|none|none|
|» balance|number(double)|false|none|Account balance point in time|
|» institutionName|string|false|none|none|
|» accountNumber|string|false|none|none|
|» routingNumber|string|false|none|none|
|» modifiedDate|string(date-time)|false|none|none|
|» status|string|false|none|Account Status|
|» verified|boolean|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|ready|
|status|closed|
|status|executed|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
automator_auth ( Scopes: write:items read:items )
</aside>

<h1 id="tradeautomator-api-content">content</h1>

## searches content

<a id="opIdsearchContent"></a>

> Code samples

```javascript
var headers = {
  'Accept':'application/json'

};

$.ajax({
  url: '/content',
  method: 'get',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/content");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Accept':'application/json'

};

$.ajax({
  url: '/content',
  method: 'get',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/content");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`GET /content`

By passing in the appropriate options, you can search for
available content in the system

<h3 id="searches-content-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|searchString|query|string|false|pass an optional search string for looking up content|
|skip|query|integer(int32)|false|number of records to skip for pagination|
|limit|query|integer(int32)|false|maximum number of records to return|

> Example responses

> 200 Response

```json
[
  {
    "id": 0,
    "name": "Widget Adapter",
    "author": {
      "id": 0,
      "fingerprint": "DK$DFSJaraDD",
      "ssn": 1112233334,
      "username": "Sparky",
      "homePage": "https://www.acme-corp.com",
      "social": "https://twitter.com/StarterIO"
    },
    "releaseDate": "2018-12-30T04:46:06.529Z",
    "organization": {
      "id": 0,
      "name": "ACME Corporation",
      "homePage": "https://www.acme-corp.com",
      "phone": "408-867-5309"
    }
  }
]
```

<h3 id="searches-content-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|search results matching criteria|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|bad input parameter|None|

<h3 id="searches-content-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Content](#schemacontent)]|false|none|none|
|» id|integer(int64)|true|none|none|
|» name|string|true|none|none|
|» author|object|true|none|none|
|»» id|integer(int64)|true|none|none|
|»» fingerprint|string|false|none|none|
|»» ssn|string|false|none|This is the value|
|»» username|string|true|none|none|
|»» homePage|string(url)|false|none|none|
|»» social|string(url)|false|none|none|
|» releaseDate|string(int32)|true|none|none|
|» organization|object|true|none|none|
|»» id|integer(int64)|true|none|none|
|»» name|string|true|none|none|
|»» homePage|string(url)|false|none|none|
|»» phone|string|false|none|none|

<aside class="success">
This operation does not require authentication
</aside>

## adds an content item

<a id="opIdaddContent"></a>

> Code samples

```javascript
var headers = {
  'Content-Type':'application/json'

};

$.ajax({
  url: '/content',
  method: 'post',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/content");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Content-Type':'application/json'

};

$.ajax({
  url: '/content',
  method: 'post',

  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/content");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`POST /content`

Adds an item to the system

> Body parameter

```json
{
  "id": 0,
  "name": "Widget Adapter",
  "author": {
    "id": 0,
    "fingerprint": "DK$DFSJaraDD",
    "ssn": 1112233334,
    "username": "Sparky",
    "homePage": "https://www.acme-corp.com",
    "social": "https://twitter.com/StarterIO"
  },
  "releaseDate": "2018-12-30T04:46:06.530Z",
  "organization": {
    "id": 0,
    "name": "ACME Corporation",
    "homePage": "https://www.acme-corp.com",
    "phone": "408-867-5309"
  }
}
```

<h3 id="adds-an-content-item-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[Content](#schemacontent)|false|Content item to add|

<h3 id="adds-an-content-item-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|item created|None|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|invalid input, object invalid|None|
|409|[Conflict](https://tools.ietf.org/html/rfc7231#section-6.5.8)|an existing item already exists|None|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="tradeautomator-api-user">user</h1>

## Logs user into the system

<a id="opIdloginUser"></a>

> Code samples

```javascript
var headers = {
  'Accept':'application/xml'

};

$.ajax({
  url: '/user/login',
  method: 'get',
  data: '?username=string&password=string',
  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/user/login?username=string&password=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript
var headers = {
  'Accept':'application/xml'

};

$.ajax({
  url: '/user/login',
  method: 'get',
  data: '?username=string&password=string',
  headers: headers,
  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/user/login?username=string&password=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`GET /user/login`

<h3 id="logs-user-into-the-system-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|username|query|string|true|The user name for login|
|password|query|string|true|The password for login in clear text|

> Example responses

> 200 Response

```json
"string"
```

<h3 id="logs-user-into-the-system-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|string|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid username/password supplied|None|

### Response Headers

|Status|Header|Type|Format|Description|
|---|---|---|---|---|
|200|X-Rate-Limit|integer|int32|calls per hour allowed by the user|
|200|X-Expires-After|string|date-time|date in UTC when token expires|

<aside class="success">
This operation does not require authentication
</aside>

## Logs out current logged in user session

<a id="opIdlogoutUser"></a>

> Code samples

```javascript

$.ajax({
  url: '/user/logout',
  method: 'get',

  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/user/logout");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```javascript

$.ajax({
  url: '/user/logout',
  method: 'get',

  success: function(data) {
    console.log(JSON.stringify(data));
  }
})

```

```java
URL obj = new URL("/user/logout");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

`GET /user/logout`

<h3 id="logs-out-current-logged-in-user-session-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|default|Default|successful operation|None|

<aside class="success">
This operation does not require authentication
</aside>

# Schemas

<h2 id="tocSaccount">Account</h2>

<a id="schemaaccount"></a>

```json
{
  "id": 0,
  "balance": 0,
  "institutionName": "string",
  "accountNumber": "string",
  "routingNumber": "string",
  "modifiedDate": "2018-12-30T04:46:06Z",
  "status": "ready",
  "verified": false
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|balance|number(double)|false|none|Account balance point in time|
|institutionName|string|false|none|none|
|accountNumber|string|false|none|none|
|routingNumber|string|false|none|none|
|modifiedDate|string(date-time)|false|none|none|
|status|string|false|none|Account Status|
|verified|boolean|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|ready|
|status|closed|
|status|executed|

<h2 id="tocSledgerentry">LedgerEntry</h2>

<a id="schemaledgerentry"></a>

```json
{
  "id": 0,
  "accountId": 0,
  "orderId": 0,
  "entryValue": 0,
  "institutionName": "string",
  "description": "string",
  "accountBalance": 0,
  "modifiedDate": "2018-12-30T04:46:06Z",
  "status": "ready",
  "transactionType": "debit",
  "verified": false
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|accountId|integer(int64)|false|none|none|
|orderId|integer(int64)|false|none|none|
|entryValue|number(double)|false|none|none|
|institutionName|string|false|none|none|
|description|string|false|none|none|
|accountBalance|number(double)|false|none|none|
|modifiedDate|string(date-time)|false|none|none|
|status|string|false|none|Transaction Status|
|transactionType|string|false|none|Transaction Type|
|verified|boolean|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|ready|
|status|closed|
|status|executed|
|transactionType|debit|
|transactionType|credit|
|transactionType|deposit|
|transactionType|void|
|transactionType|refund|

<h2 id="tocSorder">Order</h2>

<a id="schemaorder"></a>

```json
{
  "id": 0,
  "tradeId": 0,
  "quantity": 0,
  "executionDate": "2018-12-30T04:46:06Z",
  "status": "placed",
  "complete": false
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|tradeId|integer(int64)|false|none|none|
|quantity|integer(int32)|false|none|none|
|executionDate|string(date-time)|false|none|none|
|status|string|false|none|Order Status|
|complete|boolean|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|status|placed|
|status|approved|
|status|executed|

<h2 id="tocScontent">Content</h2>

<a id="schemacontent"></a>

```json
{
  "id": 0,
  "name": "Widget Adapter",
  "author": {
    "id": 0,
    "fingerprint": "DK$DFSJaraDD",
    "ssn": 1112233334,
    "username": "Sparky",
    "homePage": "https://www.acme-corp.com",
    "social": "https://twitter.com/StarterIO"
  },
  "releaseDate": "2018-12-30T04:46:06.532Z",
  "organization": {
    "id": 0,
    "name": "ACME Corporation",
    "homePage": "https://www.acme-corp.com",
    "phone": "408-867-5309"
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|name|string|true|none|none|
|author|[User](#schemauser)|true|none|none|
|releaseDate|string(int32)|true|none|none|
|organization|[Organization](#schemaorganization)|true|none|none|

<h2 id="tocSorganization">Organization</h2>

<a id="schemaorganization"></a>

```json
{
  "id": 0,
  "name": "ACME Corporation",
  "homePage": "https://www.acme-corp.com",
  "phone": "408-867-5309"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|name|string|true|none|none|
|homePage|string(url)|false|none|none|
|phone|string|false|none|none|

<h2 id="tocStrade">Trade</h2>

<a id="schematrade"></a>

```json
{
  "id": 0,
  "name": "Litespeed",
  "type": "MARKET"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|name|string|true|none|none|
|type|string|false|none|none|

<h2 id="tocSuser">User</h2>

<a id="schemauser"></a>

```json
{
  "id": 0,
  "fingerprint": "DK$DFSJaraDD",
  "ssn": 1112233334,
  "username": "Sparky",
  "homePage": "https://www.acme-corp.com",
  "social": "https://twitter.com/StarterIO"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|fingerprint|string|false|none|none|
|ssn|string|false|none|This is the value|
|username|string|true|none|none|
|homePage|string(url)|false|none|none|
|social|string(url)|false|none|none|

<h2 id="tocSworkflows">WorkFlows</h2>

<a id="schemaworkflows"></a>

```json
{
  "id": 0,
  "fingerprint": "DK$DFSJaraDD",
  "sourceType": "user",
  "sourceOwner": "userId:d290f1ee-6c54-4b01-90e6-d701748f0851",
  "json": "Sparkyt",
  "xml": "Sparkyt",
  "modifiedDate": "2018-12-30T04:46:06Z"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|fingerprint|string|false|none|none|
|sourceType|string|false|none|none|
|sourceOwner|string|false|none|none|
|json|string|true|none|this value is the JSON content of the WorkFlow definition|
|xml|string|false|none|none|
|modifiedDate|string(date-time)|false|none|none|

<h2 id="tocSexchange">Exchange</h2>

<a id="schemaexchange"></a>

```json
{
  "id": 0,
  "name": "Bitfinex",
  "ticker": "GDX",
  "lastValidatedDate": "2018-12-30T04:46:06Z",
  "organization": {
    "id": 0,
    "name": "ACME Corporation",
    "homePage": "https://www.acme-corp.com",
    "phone": "408-867-5309"
  },
  "country": "CAN"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|name|string|true|none|none|
|ticker|string|true|none|none|
|lastValidatedDate|string(date-time)|false|none|none|
|organization|[Organization](#schemaorganization)|false|none|none|
|country|string|false|none|none|

<h2 id="tocScontactmethod">ContactMethod</h2>

<a id="schemacontactmethod"></a>

```json
{
  "id": 0,
  "messageTypeDescription": "twitter handle",
  "messageType": "email",
  "createDate": "2018-12-30T04:46:06Z",
  "status": "new",
  "complete": false
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|messageTypeDescription|string|false|none|none|
|messageType|string|false|none|none|
|createDate|string(date-time)|false|none|none|
|status|string|false|none|Contact Method Status|
|complete|boolean|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|messageType|github|
|messageType|email|
|messageType|twitter|
|messageType|mobile|
|messageType|sms|
|messageType|facebook|
|status|new|
|status|validated|
|status|closed|
|status|DNC|
|status|ADMIN|

<h2 id="tocSauditlog">AuditLog</h2>

<a id="schemaauditlog"></a>

```json
{
  "id": 0,
  "fingerprint": "DK$DFSJaraDD",
  "sourceType": "user",
  "sourceOwner": "userId:1000",
  "json": "Sparkyt",
  "modifiedDate": "2018-12-30T04:46:06Z"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|fingerprint|string|false|none|none|
|sourceType|string|true|none|none|
|sourceOwner|string|false|none|none|
|json|string|true|none|none|
|modifiedDate|string(date-time)|false|none|none|

<h2 id="tocSdata">Data</h2>

<a id="schemadata"></a>

```json
{
  "id": 0,
  "fingerprint": "DK$DFSJaraDD",
  "sourceType": "user",
  "sourceOwner": "userId:1000",
  "json": "Sparkyt",
  "modifiedDate": "2018-12-30T04:46:06Z"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|fingerprint|string|false|none|none|
|sourceType|string|true|none|none|
|sourceOwner|string|false|none|none|
|json|string|true|none|none|
|modifiedDate|string(date-time)|false|none|none|

<h2 id="tocScategory">Category</h2>

<a id="schemacategory"></a>

```json
{
  "id": 0,
  "name": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|name|string|false|none|none|

<h2 id="tocStag">Tag</h2>

<a id="schematag"></a>

```json
{
  "id": 0,
  "name": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|name|string|false|none|none|

<h2 id="tocSitem">Item</h2>

<a id="schemaitem"></a>

```json
{
  "id": 0,
  "category": {
    "id": 0,
    "name": "string"
  },
  "name": "doggie",
  "photoUrls": [
    "string"
  ],
  "tags": [
    {
      "id": 0,
      "name": "string"
    }
  ],
  "status": "available"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|category|[Category](#schemacategory)|false|none|none|
|name|string|true|none|none|
|photoUrls|[string]|true|none|none|
|tags|[[Tag](#schematag)]|false|none|none|
|status|string|false|none|item status in the store|

#### Enumerated Values

|Property|Value|
|---|---|
|status|available|
|status|pending|
|status|sold|

<h2 id="tocSapiresponse">ApiResponse</h2>

<a id="schemaapiresponse"></a>

```json
{
  "id": 0,
  "code": 0,
  "type": "string",
  "message": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|code|integer(int32)|false|none|none|
|type|string|false|none|none|
|message|string|false|none|none|
