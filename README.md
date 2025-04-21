# Advertisement Service API
This service provides RESTful endpoints to manage advertisements. It supports creating, retrieving, updating, and deleting advertisements, along with listing them with pagination.

##  Database Setup (MongoDB)
This application uses MongoDB. Before running the service for the first time, make sure your MongoDB instance has the correct database and user credentials.

You can create them using the following command:
use adsdb
db.createUser({
  user: "myuser",
  pwd: "mypassword",
  roles: [{ role: "readWrite", db: "adsdb" }]
})

Alternatively, update the credentials in your `application.properties` file.

---
## Base Configuration

- **Base URL**: `/api/v1/advertisements`
- **Application Port**: `9999`

---
## Endpoints

### 1. Get Advertisement by ID

- **Method**: `GET`
- **URL**: `/api/v1/advertisements/{ad_id}`
- **Description**: Retrieve a single advertisement using its ID.

#### Example Response
{
  "title": "Special Offer",
  "content": "Come and visit our site for the offer",
  "mediaUrl": "http://example.com/image.jpg",
  "mediaType": "IMAGE",
  "startDate": "2025-04-21T00:00:00Z",
  "endDate": "2025-04-30T00:00:00Z"
}

---
### 2. Get All Advertisements (Paginated)

- **Method**: `GET`
- **URL**: `/api/v1/advertisements`
- **Description**: Returns a paginated list of advertisements.

#### Example Response
{
  "content": [{ /* Advertisement JSON */ }],
  "totalElements": 5,
  "totalPages": 1,
  "number": 0,
  "size": 10
}

---
### 3. Create Advertisement

- **Method**: `POST`
- **URL**: `/api/v1/advertisements`
- **Description**: Creates a new advertisement.

#### Request Body
{
  "title": "Spring Sale",
  "content": "Come and visit our site for the offer",
  "mediaUrl": "https://example.com/banner.jpg",
  "mediaType": "IMAGE",
  "startDate": "2025-04-21T00:00:00Z",
  "endDate": "2025-04-30T00:00:00Z"
}

#### Response
- `200 OK` with the created advertisement.

---
### 4. Update Advertisement

- **Method**: `PUT`
- **URL**: `/api/v1/advertisements/{ad_id}`
- **Description**: Updates an existing advertisement.

#### Request Body
{
  "title": "Spring Sale Updated",
  "content": "Now with 50% off!",
  "mediaUrl": "https://example.com/new-banner.jpg",
  "mediaType": "IMAGE",
  "startDate": "2025-04-21T00:00:00Z",
  "endDate": "2025-05-01T00:00:00Z"
}

#### Response
- `200 OK` with the updated advertisement.

---
### 5. Delete Advertisement

- **Method**: `DELETE`
- **URL**: `/api/v1/advertisements/{ad_id}`
- **Description**: Deletes the specified advertisement.

#### Response
200 OK "deleted!"


---
## Notes
- Pagination defaults to page `0` and size `10`.
- Media can be either an image or a video, defined by the `mediaType` field.
