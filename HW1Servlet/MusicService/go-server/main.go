package main

import (
	"github.com/gin-gonic/gin"
	"mime/multipart"
	"net/http"
	"strconv"
	"strings"
)

// ImageMetaData For the POST request body
type ImageMetaData struct {
	Image   *multipart.FileHeader `form:"image"`
	Profile string                `form:"profile"`
}

// AlbumPostResponse For the POST response
type AlbumPostResponse struct {
	AlbumID   string `json:"albumID"`
	ImageSize string `json:"imageSize"`
}

// ErrorResponse Error message structure for both GET and POST
type ErrorResponse struct {
	Msg string `json:"msg"`
}

type AlbumInfo struct {
	Artist string `form:"artist"`
	Title  string `form:"title"`
	Year   string `form:"year"`
}

var albums = []AlbumInfo{
	{Artist: "Sex Pistols", Title: "Never Mind The Bollocks!", Year: "1977"},
}

func main() {
	r := gin.Default()

	r.POST("/albums", handleAlbumPost)
	r.GET("/albums/:albumID", handleAlbumGet)

	r.Run(":8080") // listen and serve on port 8080
}

func handleAlbumPost(c *gin.Context) {
	var requestBody ImageMetaData

	if err := c.ShouldBind(&requestBody); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// Check if the image and profile are provided
	if requestBody.Image == nil || requestBody.Profile == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Image and profile are required"})
		return
	}

	// Validate the presence of required fields in requestBody.Profile
	requiredFields := []string{"artist", "title", "year"}
	for _, field := range requiredFields {
		if !strings.Contains(requestBody.Profile, field+":") {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Missing field: " + field})
			return
		}
	}

	// Parse the JSON profile
	//var profile AlbumInfo
	//if err := json.Unmarshal([]byte(requestBody.Profile), &profile); err != nil {
	//	fmt.Println("Error parsing JSON:", err.Error())
	//	c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid profile JSON"})
	//	return
	//}

	// Validate the profile fields
	//if profile.Artist == "" || profile.Title == "" || profile.Year == "" {
	//	c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid profile fields"})
	//	return
	//}

	// Here, you're simply returning a fixed album key (example: "12345") and size of the image in bytes.
	// Ideally, you'd save the data and return a meaningful ID and size, but this is a stub.
	response := AlbumPostResponse{
		AlbumID:   "dummyid12345",
		ImageSize: strconv.Itoa(int(requestBody.Image.Size)),
	}
	c.JSON(http.StatusOK, response)
}

func handleAlbumGet(c *gin.Context) {
	albumID := c.Param("albumID")

	// Validate albumID if necessary. Here we are just checking if it's non-empty.
	if albumID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid album ID"})
		return
	}

	c.IndentedJSON(http.StatusOK, albums[0])
}
