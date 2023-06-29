package dtos

type OrderDto struct {
	Id        int64  `json:"id"`
	OrderCode string `json:"orderCode"`
	Division  string `json:"division"`
}
