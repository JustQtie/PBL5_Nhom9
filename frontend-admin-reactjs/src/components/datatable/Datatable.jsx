import "./datatable.scss"
import { DataGrid } from '@mui/x-data-grid';
import { userColumns, userRows } from "../../datatablesource";
import { Link } from "react-router-dom";
import { useState } from "react";
const Datatable = () => {
    const [data, setData] = useState(userRows);

    const handleDelete = (id) => {
        setData(data.filter((item) => item.id !== id));
    }

    const actionColumn = [{
        field: "action", headerName: "Action", width: 170, renderCell: (params) => {
            return (
                <div className="cellAction">
                    <Link to="/qlnguoidung/test" style={{ textDecoration: "none" }}>
                        <button className="viewButton">View</button>
                    </Link>
                    <button className="deleteButton" onClick={() => handleDelete(params.row.id)}>Ban</button>

                </div>
            );
        }
    }];

    return (
        <div className="datatable">
            <div className="datatableTitle">
                Quản Lý Người Dùng
                <Link to="/qlnguoidung/new" style={{ textDecoration: "none" }} className="link">
                    Thêm mới
                </Link>
            </div>
            <DataGrid
                className="datagrid"
                rows={data}
                columns={userColumns.concat(actionColumn)}
                pageSize={9}
                pagePerPageOptions={[9]}
                checkboxSelection
            />
        </div>
    );
};

export default Datatable;