// Componente: barra de paginación con ventana deslizante de 3 páginas
import Pagination from 'react-bootstrap/Pagination';

function Paginador({ paginacion, onCambioPagina }) {
    
  if (!paginacion || paginacion.totalPages <= 1) {
    return null;
  }

  const { number: currentPage, totalPages } = paginacion;
  const lastPage = totalPages - 1;

  // Para que no aparezca una pagina que no existe
  let startPage = Math.max(0, currentPage - 1);
  let endPage = Math.min(lastPage, currentPage + 1);

  // Para tener siempre 3 paginas visibles
  if (currentPage === 0) {
    endPage = Math.min(lastPage, 2);
  } else if (currentPage === lastPage) {
    startPage = Math.max(0, lastPage - 2);
  }

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  const handlePageClick = (pageIndex) => {
    if (pageIndex !== currentPage && pageIndex >= 0 && pageIndex <= lastPage) {
      onCambioPagina(pageIndex);
    }
  };

  return (
    <Pagination size="sm" className="justify-content-center mt-4 flex-wrap">
      <Pagination.First onClick={() => handlePageClick(0)} disabled={currentPage === 0} />
      <Pagination.Prev onClick={() => handlePageClick(currentPage - 1)} disabled={currentPage === 0} />

      {startPage > 0 && (
        <>
          <Pagination.Item onClick={() => handlePageClick(0)}>{1}</Pagination.Item>
          {startPage > 1 && <Pagination.Ellipsis disabled />}
        </>
      )}

      {pages.map((pageIndex) => (
        <Pagination.Item
          key={pageIndex}
          active={pageIndex === currentPage}
          onClick={() => handlePageClick(pageIndex)}
        >
          {pageIndex + 1} {/* Sumamos 1 solo para la vista del usuario */}
        </Pagination.Item>
      ))}

      {endPage < lastPage && (
        <>
          {endPage < lastPage - 1 && <Pagination.Ellipsis disabled />}
          <Pagination.Item onClick={() => handlePageClick(lastPage)}>
            {totalPages}
          </Pagination.Item>
        </>
      )}

      <Pagination.Next onClick={() => handlePageClick(currentPage + 1)} disabled={currentPage === lastPage} />
      <Pagination.Last onClick={() => handlePageClick(lastPage)} disabled={currentPage === lastPage} />
    </Pagination>
  );
}

export default Paginador;