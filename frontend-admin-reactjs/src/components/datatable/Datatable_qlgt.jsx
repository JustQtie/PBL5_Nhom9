import "./datatable_qlgt.scss"
import { Link } from "react-router-dom";
import { useState } from "react";
import TableProduct from "./TableProduct";

const Datatable = () => {


    return (
        <div className="datatable-qlgt">
            <div className="datatableTitle-qlgt">
                Quản Lý Giáo trình
            </div>
            <div>
                <TableProduct />
            </div>
        </div>
    );
};

export default Datatable;