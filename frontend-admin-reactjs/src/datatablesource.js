export const userColumns = [
    { field: "id", headerName: "ID", width: 70 },
    {
        field: "user", headerName: "User", width: 230, renderCell: (params) => {
            return (
                <div className="cellWithImg">
                    <img className="cellImg" src={params.row.img} alt="avatar" />
                    {params.row.username}
                </div>
            )
        }
    },
    {
        field: "email", headerName: "Email", width: 350

    },
    {
        field: "age", headerName: "Age", width: 100

    },
    {
        field: "status", headerName: "Status", width: 160,
        renderCell: (params) => {
            return (
                <span className={`cellWithStatus ${params.row.status}`}>
                    {params.row.status}
                </span>
            )
        },

    },
];

export const userRows = [
    {
        id: 1,
        username: "hovanthao",
        img: "https://i.imgur.com/1nORATT.png",
        email: "hovanthao0611@gmail.com",
        status: "active",
        age: 18,
    },
    {
        id: 2,
        username: "hoangnguyennhatminh",
        img: "https://i.imgur.com/Z7I5dGq.png",
        email: "hoangnguyennhatminh982@gmail.com",
        status: "passive",
        age: 19,
    },
    {
        id: 3,
        username: "tranminhquan",
        img: "https://i.imgur.com/ow5AhgA.png",
        email: "tranminhquan890@gmail.com",
        status: "active",
        age: 21,
    },
    {
        id: 4,
        username: "voviettruong",
        img: "https://i.imgur.com/jdSQfdm.png",
        email: "voviettruong728@gmail.com",
        status: "pending",
        age: 25,
    },
    {
        id: 5,
        username: "nguyenanhquan",
        img: "https://i.imgur.com/MUUp1Fa.png",
        email: "nguyenanhquan392@gmail.com",
        status: "passive",
        age: 23,
    },
    {
        id: 6,
        username: "nguyenanhquan",
        img: "https://i.imgur.com/MUUp1Fa.png",
        email: "nguyenanhquan392@gmail.com",
        status: "passive",
        age: 23,
    },

    {
        id: 7,
        username: "nguyenanhquan",
        img: "https://i.imgur.com/MUUp1Fa.png",
        email: "nguyenanhquan392@gmail.com",
        status: "passive",
        age: 23,
    },
    {
        id: 8,
        username: "nguyenanhquan",
        img: "https://i.imgur.com/MUUp1Fa.png",
        email: "nguyenanhquan392@gmail.com",
        status: "passive",
        age: 23,
    },
    {
        id: 9,
        username: "hoangnguyennhatminh",
        img: "https://i.imgur.com/Z7I5dGq.png",
        email: "hoangnguyennhatminh982@gmail.com",
        status: "passive",
        age: 19,
    },
    {
        id: 10,
        username: "tranminhquan",
        img: "https://i.imgur.com/ow5AhgA.png",
        email: "tranminhquan890@gmail.com",
        status: "active",
        age: 21,
    },
    {
        id: 11,
        username: "voviettruong",
        img: "https://i.imgur.com/jdSQfdm.png",
        email: "voviettruong728@gmail.com",
        status: "pending",
        age: 25,
    },
    {
        id: 12,
        username: "nguyenanhquan",
        img: "https://i.imgur.com/MUUp1Fa.png",
        email: "nguyenanhquan392@gmail.com",
        status: "passive",
        age: 23,
    },

];


