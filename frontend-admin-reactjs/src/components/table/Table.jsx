import "./table.scss";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

const List = () => {
    const rows = [
        {
            id: 4,
            product: "Bài giảng giải tích 1",
            img: "https://i.imgur.com/3nK2sud.jpeg",
            customer: "Võ Viết Trường",
            date: "11/06/2024",
            amount: 5,
            method: "Chuyển khoản",
            status: "Approved",

        },
        {
            id: 1,
            product: "Giáo trình giải tích 2",
            img: "https://i.imgur.com/5ckLztH.jpeg",
            customer: "Hoàng Nguyễn Nhật Minh",
            date: "11/06/2024",
            amount: 4,
            method: "Chuyển khoản",
            status: "Pending",

        },
        {
            id: 6,
            product: "Giáo trình Đại số tuyến tính",
            img: "https://i.imgur.com/JrKLZfZ.jpeg",
            customer: "Hồ Văn Thảo",
            date: "11/06/2024",
            amount: 2,
            method: "Chuyển khoản",
            status: "Approved",

        }, {
            id: 3,
            product: "Giáo trình lịch sử đảng",
            img: "https://i.imgur.com/FLbs9DP.jpeg",
            customer: "Nguyễn Anh Quân",
            date: "11/06/2024",
            amount: 5,
            method: "Chuyển khoản",
            status: "Pending",

        }
    ]
    return (
        <TableContainer component={Paper} className="table">
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell className="tableCell">ID</TableCell>
                        <TableCell className="tableCell">Tiêu đề</TableCell>
                        <TableCell className="tableCell">Người bán</TableCell>
                        <TableCell className="tableCell">Ngày</TableCell>
                        <TableCell className="tableCell">Số lượng</TableCell>
                        <TableCell className="tableCell">Phương thức thanh toán</TableCell>
                        <TableCell className="tableCell">Trạng thái</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow key={row.id}>
                            <TableCell className="tableCell">{row.id}</TableCell>
                            <TableCell className="tableCell">
                                <div className="cellWrapper">
                                    <img src={row.img} alt="" className="image" />
                                    {row.product}
                                </div>
                            </TableCell>
                            <TableCell className="tableCell">{row.customer}</TableCell>
                            <TableCell className="tableCell">{row.date}</TableCell>
                            <TableCell className="tableCell">{row.amount}</TableCell>
                            <TableCell className="tableCell">{row.method}</TableCell>
                            <TableCell className="tableCell">
                                <span className={`status ${row.status}`}>{row.status}</span>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default List;