package actions

import (
	"buffalo_bench/services"
	"github.com/gobuffalo/buffalo"
	"net/http"
)

func CreateAndGetHandler(c buffalo.Context) error {
	services.Create()
	return c.Render(http.StatusOK, r.JSON(services.GetOne()))
}
