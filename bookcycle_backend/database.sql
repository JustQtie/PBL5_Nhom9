CREATE DATABASE bookcycle;
USE bookcycle;

CREATE TABLE Role(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
)

CREATE TABLE Users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(100) NOT NULL DEFAULT '',
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    role_id INT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    gender TINYINT(1),
    thumbnail VARCHAR(255),
    FOREIGN KEY(role_id) REFERENCES roles(id)
)


CREATE TABLE Tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL, -- UNIQUE Chỉ định rằng giá trị trong cột token phải là duy nhất trong toàn bộ bảng.
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL, --thu hồi
    expired TINYINT(1) NOT NULL, --hết hạn
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Bảng danh mục sản phẩm.
CREATE TABLE Categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'Tên sách'
);

-- Bảng sản phẩm.
CREATE TABLE Products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL DEFAULT '',
    author VARCHAR(255) NOT NULL DEFAULT '',
    price FLOAT NOT NULL,
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '' ,
    point FLOAT DEFAULT 0, 
    quantity INT NOT NULL,
    status ENUM('Đã sử dụng trong vòng 6 tháng', 'Đã sử dụng hơn 1 năm', 'Đã sử dụng từ 3 đến 5 năm', 'Đã sử dụng hơn 5 năm') COMMENT 'Tình trạng sách',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    user_id INT,
    FOREIGN KEY (category_id) REFERENCES Categories(id)
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_product_images_product_id
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE -- Khi một product bị xóa thì các ảnh của nó sẽ bị xóa.
    image_url VARCHAR(300)
);

-- Bang Order:
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    status ENUM('saving', 'pending', 'confirmed', 'paid')
COMMENT 'Trạng thái đơn hàng',
    number_of_product INT,
    total_money FLOAT,
    shipping_address VARCHAR(200),
    payment_method VARCHAR(100),
    active TINYINT(1),
);

CREATE TABLE Notify(
    id INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT DEFAULT '',
    status ENUM('agree', 'cancel', 'have not responded yet'),
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
)

CREATE TABLE Comment(
    id INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    product_id INT,
    user_id INT,
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
)