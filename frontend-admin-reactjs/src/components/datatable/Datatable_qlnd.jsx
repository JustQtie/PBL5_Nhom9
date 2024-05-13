import "./datatable_qlnd.scss"
import { useState } from "react";
import TableUser from "./TableUser";

const Datatable = () => {


    return (
        <div className="datatable">
            <div className="datatableTitle">
                Quản Lý Người Dùng
            </div>
            <div>
                <TableUser />
            </div>
        </div>
    );
};

export default Datatable;