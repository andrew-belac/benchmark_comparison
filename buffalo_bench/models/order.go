package models

import (
	"time"
)

type Order struct {
	ID        int64     `db:"id"`
	OrderCode string    `db:"order_code"`
	Division  string    `db:"division"`
	CreatedAt time.Time `db:"created_at"`
	UpdatedAt time.Time `db:"updated_at"`
}
