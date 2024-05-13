export const userColumns = [
    { field: "id", headerName: "ID", width: 70 },
    {
        field: "tieuDe", headerName: "Tiêu đề", width: 230, renderCell: (params) => {
            return (
                <div className="cellWithImg">
                    <img className="cellImg" src={params.row.img} alt="avatar" />
                    {params.row.tieuDe}
                </div>
            )
        }
    },
    {
        field: "loai", headerName: "Loại", width: 100

    },
    {
        field: "gia", headerName: "Giá", width: 100

    },
];

export const userRows = [
    {
        id: 1,
        tieuDe: "Đại số tuyến tính",
        img: "https://i.imgur.com/JrKLZfZ.jpg",
        loai: "giáo trình",
        gia: "5$",
    },
    {
        id: 2,
        tieuDe: "Lịch sử đảng",
        img: "https://i.imgur.com/FLbs9DP.jpg",
        loai: "giáo trình",
        gia: "5$",
    },
    {
        id: 3,
        tieuDe: "Giả tích 1",
        img: "https://i.imgur.com/3nK2sud.jpg",
        loai: "giáo trình",
        gia: "5$",
    },
    {
        id: 4,
        tieuDe: "Giải tích 2",
        img: "https://i.imgur.com/5ckLztH.jpg",
        loai: "giáo trình",
        gia: "5$",
    },
];


