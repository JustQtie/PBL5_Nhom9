import "./datatable_qlgt.scss"
import { DataGrid } from '@mui/x-data-grid';
import { userColumns, userRows } from "../../datatablesource_giaotrinh";
import { Link } from "react-router-dom";
import { useState } from "react";
const Datatable = () => {
    const [data, setData] = useState(userRows);

    // const handleDelete = (id) => {
    //     setData(data.filter((item) => item.id !== id));
    // }

    const actionColumn = [{
        field: "action", headerName: "Action", width: 170, renderCell: (params) => {
            return (
                <div className="cellAction-Datatable-qlgt">
                    <Link to="/qlgiaotrinh/product" style={{ textDecoration: "none" }}>
                        <button className="viewButton-Datatable-qlgt">View</button>
                    </Link>
                </div>
            );
        }
    }];

    return (
        <div className="datatable-qlgt">
            <div className="datatableTitle-qlgt">
                Quản Lý Giáo trình
            </div>
            <DataGrid
                className="datagrid-qldt"
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