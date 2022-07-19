CREATE TABLE "tbl_category" (
  "category_id" SERIAL PRIMARY KEY,
  "category_name" varchar
);

CREATE TABLE "tbl_product" (
  "product_id" SERIAL PRIMARY KEY,
  "product_name" varchar
);

CREATE TABLE "tbl_product_cateagory_mapping" (
  "product_category_mapping_id" SERIAL PRIMARY KEY,
  "category_id" int,
  "product_id" int
);

CREATE TABLE "tbl_user" (
  "user_id" SERIAL PRIMARY KEY,
  "name" varchar,
  "phone_no" varchar,
  "email" varchar,
  "photo_url" varchar
);

CREATE TABLE "tbl_user_pc_map_mapping" (
  "user_pc_mapping_id" SERIAL PRIMARY KEY,
  "user_id" int,
  "pc_map_id" int
);

CREATE TABLE "tbl_shopping" (
  "shopping_id" SERIAL PRIMARY KEY,
  "shopping_time" timestamp
);

CREATE TABLE "tbl_shopping_items" (
  "product_id" int,
  "shopping_id" int,
  "price" int,
  "desc" varchar
);

ALTER TABLE "tbl_product_cateagory_mapping" ADD FOREIGN KEY ("product_id") REFERENCES "tbl_product" ("product_id");

ALTER TABLE "tbl_product_cateagory_mapping" ADD FOREIGN KEY ("category_id") REFERENCES "tbl_category" ("category_id");

ALTER TABLE "tbl_user_pc_map_mapping" ADD FOREIGN KEY ("user_id") REFERENCES "tbl_user" ("user_id");

ALTER TABLE "tbl_user_pc_map_mapping" ADD FOREIGN KEY ("pc_map_id") REFERENCES "tbl_product_cateagory_mapping" ("product_category_mapping_id");

ALTER TABLE "tbl_shopping_items" ADD FOREIGN KEY ("shopping_id") REFERENCES "tbl_shopping" ("shopping_id");

ALTER TABLE "tbl_shopping_items" ADD FOREIGN KEY ("product_id") REFERENCES "tbl_product" ("product_id");
